package com.example.junguniv_bb._core.permission;

import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, ApplicationListener<ManagerMenuChangeEvent> {

    private final ManagerMenuRepository managerMenuRepository;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private volatile Map<String, ManagerMenu> urlToMenuMap = new ConcurrentHashMap<>();

    // API 엔드포인트 패턴 (예: /api/users, /api/posts, /api?version=1 등 /api를 포함한 모든 요청)
    private static final String API_PATTERN = "**/api**";
    // 웹 페이지 엔드포인트 패턴 (모든 웹 요청에 대한 패턴)
    private static final String WEB_PATTERN = "/**";

    // 개발 모드 플래그 추가
    private static final boolean IS_DEV_MODE = false; // TODO 개발 완료 후 false로 변경

    public DynamicSecurityMetadataSource(ManagerMenuRepository managerMenuRepository) {
        this.managerMenuRepository = managerMenuRepository;
        loadResourceDefine();
    }

    // 메뉴 데이터를 로드하여 맵에 저장
    private synchronized void loadResourceDefine() {
        List<ManagerMenu> menus = managerMenuRepository.findAll();
        Map<String, ManagerMenu> newMap = new HashMap<>();

        for (ManagerMenu menu : menus) {
            String url = menu.getUrl();

            // 3차 메뉴만 실제 권한 체크 대상
            if (menu.getMenuLevel() == 3 && url != null && !url.isEmpty() && !url.equals("#")) {
                String urlPattern = url;
                if (url.startsWith("/masterpage_sys/")) {
                    String[] segments = url.split("/", 4);
                    if (segments.length >= 3) {
                        urlPattern = String.format("/%s/%s/**", segments[1], segments[2]);
                    }
                }
                newMap.put(urlPattern, menu);
                log.debug("URL 매핑 추가: {} -> {}", urlPattern, menu.getMenuName());
            }
        }

        urlToMenuMap = newMap;
        log.info("DynamicSecurityMetadataSource 메뉴 URL 맵 갱신 완료: {}", urlToMenuMap.keySet());
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 개발 모드일 때는 모든 요청 허용
        if (IS_DEV_MODE) {
            return SecurityConfig.createList("PERMIT_ALL");
        }

        // 기존 권한 체크 로직
        FilterInvocation fi = (FilterInvocation) object;
        String uri = fi.getRequest().getRequestURI();
        String contextPath = fi.getRequest().getContextPath();
        if (!StringUtils.isEmpty(contextPath)) {
            uri = uri.substring(contextPath.length());
        }

        // 회원가입 페이지와 로그인 페이지는 명확히 허용
        if ("/member/joinForm".equals(uri) || "/login".equals(uri)) {
            return SecurityConfig.createList("PERMIT_ALL");
        }

        for (Map.Entry<String, ManagerMenu> entry : urlToMenuMap.entrySet()) {
            String menuUrl = entry.getKey();
            if (antPathMatcher.match(menuUrl, uri)) {
                ManagerMenu menu = entry.getValue();
                return List.of(new MenuConfigAttribute(menu));
            }
        }

        // 매칭되는 메뉴가 없을 경우 접근 허용
        return SecurityConfig.createList("PERMIT_ALL");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        // 모든 ConfigAttribute를 반환하거나 null을 반환할 수 있습니다.
        // 여기서는 null을 반환합니다.
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        // 이 SecurityMetadataSource가 주어진 클래스 타입을 지원하는지 여부를 반환
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public void onApplicationEvent(ManagerMenuChangeEvent event) {
        log.info("ManagerMenu 변경 이벤트 수신, 메뉴 URL 맵을 갱신합니다.");
        loadResourceDefine();
    }
}
