// package com.example.junguniv_bb._core.permission;
//
// import com.example.junguniv_bb._core.security.CustomUserDetails;
// import com.example.junguniv_bb.domain.managerMenu.model.ManagerMenu;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.access.AccessDecisionManager;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.access.ConfigAttribute;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.web.FilterInvocation;
//
// import java.util.Collection;
//
// @Slf4j
// public class CustomAccessDecisionManager implements AccessDecisionManager {
//
//     private final ManagerAuthService managerAuthService;
//
//     public CustomAccessDecisionManager(ManagerAuthService managerAuthService) {
//         this.managerAuthService = managerAuthService;
//     }
//
//     @Override
//     public void decide(Authentication authentication, Object object,
//             Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
//         if (configAttributes == null) {
//             return;
//         }
//
//         for (ConfigAttribute attribute : configAttributes) {
//             if ("PERMIT_ALL".equals(attribute.getAttribute())) {
//                 return;
//             }
//
//             if (!(attribute instanceof MenuConfigAttribute menuConfigAttribute)) {
//                 continue;
//             }
//
//             CustomUserDetails userDetails = validateAndGetUserDetails(authentication);
//             FilterInvocation fi = (FilterInvocation) object;
//             checkPermission(userDetails, fi, menuConfigAttribute);
//             return;
//         }
//
//         throw new AccessDeniedException("접근이 거부되었습니다.");
//     }
//
//     private CustomUserDetails validateAndGetUserDetails(Authentication authentication) {
//         if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
//             throw new AccessDeniedException("유효하지 않은 사용자입니다.");
//         }
//         return userDetails;
//     }
//
//     private void checkPermission(CustomUserDetails userDetails, FilterInvocation fi, MenuConfigAttribute menuConfigAttribute) {
//         String uri = fi.getRequest().getRequestURI();
//         String httpMethod = fi.getRequest().getMethod();
//         Long authLevel = userDetails.getAuthLevel();
//         ManagerMenu menu = menuConfigAttribute.getManagerMenu();
//         Long menuIdx = menu.getMenuIdx();
//
//         // CustomUserDetails에 현재 메뉴 정보 설정
//         userDetails.setCurrentMenuIdx(menuIdx);
//
//         // 기본 READ 권한 체크
//         String action = mapHttpMethodToAction(httpMethod);
//         validatePermission(menuIdx, authLevel, action, "접근이 거부되었습니다.");
//
//         // 개인정보가 포함된 메뉴인 경우 추가 검증 (단, 접근 차단하지 않음)
//         if ("Y".equals(menu.getChkPerson())) {
//             boolean hasPrivacyAccess = managerAuthService.hasPrivacyPermission(menuIdx, authLevel);
//             // log.info("개인정보 접근 권한 체크 - menuIdx: {}, authLevel: {}, hasPrivacyAccess: {}", menuIdx, authLevel, hasPrivacyAccess);
//
//             // 개인정보 접근 권한 여부를 CustomUserDetails에 저장
//             userDetails.setHasPrivacyAccess(hasPrivacyAccess);
//         }
//
//         // Form 페이지 접근 권한 체크
//         if (uri.contains("/saveForm")) {
//             validatePermission(menuIdx, authLevel, "WRITE", "등록 페이지 접근이 거부되었습니다.");
//         }
//     }
//
//     private void validatePermission(Long menuIdx, Long authLevel, String action, String errorMessage) {
//         // log.info("권한 체크 - menuIdx: {}, authLevel: {}, action: {}", menuIdx, authLevel, action);
//         if (!managerAuthService.hasPermission(menuIdx, authLevel, action)) {
//             throw new AccessDeniedException(errorMessage);
//         }
//     }
//
//     @Override
//     public boolean supports(ConfigAttribute attribute) {
//         return attribute instanceof MenuConfigAttribute || "PERMIT_ALL".equals(attribute.getAttribute());
//     }
//
//     @Override
//     public boolean supports(Class<?> clazz) {
//         return FilterInvocation.class.isAssignableFrom(clazz);
//     }
//
//     private String mapHttpMethodToAction(String httpMethod) {
//         return switch (httpMethod) {
//             case "GET" -> "READ";
//             case "POST" -> "WRITE";
//             case "PUT", "PATCH" -> "MODIFY";
//             case "DELETE" -> "DELETE";
//             default -> "UNKNOWN";
//         };
//     }
// }
