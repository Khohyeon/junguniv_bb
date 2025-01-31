package com.example.junguniv_bb._core.permission;

import com.example.junguniv_bb._core.security.CustomUserDetails;
import com.example.junguniv_bb._core.util.RequestUtils;
import com.example.junguniv_bb.domain.managerauth.service.ManagerAuthService;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

@Slf4j
public class CustomAccessDecisionManager implements AccessDecisionManager {

    private final ManagerAuthService managerAuthService;

    public CustomAccessDecisionManager(ManagerAuthService managerAuthService) {
        this.managerAuthService = managerAuthService;
    }

    @Override
    public void decide(Authentication authentication, Object object,
            Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
        if (configAttributes == null) {
            return;
        }

        for (ConfigAttribute attribute : configAttributes) {
            if ("PERMIT_ALL".equals(attribute.getAttribute())) {
                return;
            }

            if (!(attribute instanceof MenuConfigAttribute menuConfigAttribute)) {
                continue;
            }

            CustomUserDetails userDetails = validateAndGetUserDetails(authentication);
            FilterInvocation fi = (FilterInvocation) object;
            checkPermission(userDetails, fi, menuConfigAttribute);
            return;
        }

        throw new AccessDeniedException("접근이 거부되었습니다.");
    }

    private CustomUserDetails validateAndGetUserDetails(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new AccessDeniedException("유효하지 않은 사용자입니다.");
        }
        return userDetails;
    }

    private void checkPermission(CustomUserDetails userDetails, FilterInvocation fi, MenuConfigAttribute menuConfigAttribute) {
        String uri = fi.getRequest().getRequestURI();
        String httpMethod = fi.getRequest().getMethod();
        Long authLevel = userDetails.getAuthLevel();
        ManagerMenu menu = menuConfigAttribute.getManagerMenu();
        Long menuIdx = menu.getMenuIdx();

        log.debug("권한 체크 시작 - URI: {}, Method: {}", uri, httpMethod);

        // CustomUserDetails에 현재 메뉴 정보 설정
        userDetails.setCurrentMenuIdx(menuIdx);

        // API 요청인 경우 CRUD 권한 체크
        boolean isApi = RequestUtils.isApiRequest(fi.getRequest());
        log.debug("API 요청 여부: {}", isApi);

        if (isApi) {
            String action = RequestUtils.getCrudOperation(fi.getRequest());
            log.info("API 요청 권한 체크 - URI: {}, Method: {}, Action: {}, AuthLevel: {}, MenuIdx: {}", 
                uri, httpMethod, action, authLevel, menuIdx);
            validatePermission(menuIdx, authLevel, action, action + " 권한이 없습니다.");
            return;
        }

        // 웹 페이지 요청인 경우 기본 READ 권한 체크
        log.debug("웹 페이지 요청 권한 체크 시작");
        validatePermission(menuIdx, authLevel, "READ", "접근이 거부되었습니다.");
        
        // 개인정보가 포함된 메뉴인 경우 추가 검증 (단, 접근 차단하지 않음)
        if ("Y".equals(menu.getChkPerson())) {
            boolean hasPrivacyAccess = managerAuthService.hasPrivacyPermission(menuIdx, authLevel);
            log.info("개인정보 접근 권한 체크 - menuIdx: {}, authLevel: {}, hasPrivacyAccess: {}", menuIdx, authLevel, hasPrivacyAccess);

            // 개인정보 접근 권한 여부를 CustomUserDetails에 저장
            userDetails.setHasPrivacyAccess(hasPrivacyAccess);
        }

        // Form 페이지 접근 권한 체크
        if (uri.contains("/save")) {
            log.debug("등록 페이지 접근 권한 체크");
            validatePermission(menuIdx, authLevel, "WRITE", "등록 페이지 접근이 거부되었습니다.");
        } else if (uri.matches(".*/\\d+$")) { // 상세 페이지 접근 권한 체크 (URL이 숫자로 끝나는 경우)
            log.debug("상세 페이지 접근 권한 체크");
            validatePermission(menuIdx, authLevel, "READ", "상세 페이지 접근이 거부되었습니다.");
        }
    }

    private void validatePermission(Long menuIdx, Long authLevel, String action, String errorMessage) {
        log.info("권한 체크 - menuIdx: {}, authLevel: {}, action: {}", menuIdx, authLevel, action);
        if (!managerAuthService.hasPermission(menuIdx, authLevel, action)) {
            throw new AccessDeniedException(errorMessage);
        }
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute instanceof MenuConfigAttribute || "PERMIT_ALL".equals(attribute.getAttribute());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
