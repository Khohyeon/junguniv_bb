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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, ApplicationListener<ManagerMenuChangeEvent> {

    private final ManagerMenuRepository managerMenuRepository;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private volatile Map<String, ManagerMenu> urlToMenuMap = new ConcurrentHashMap<>();
    
    // URL 매칭 결과 캐시
    private final Map<String, Collection<ConfigAttribute>> matchCache = new ConcurrentHashMap<>();

    // 개발 모드 플래그 추가
    private static final boolean IS_DEV_MODE = true; // TODO 개발 완료 후 false로 변경

    public DynamicSecurityMetadataSource(ManagerMenuRepository managerMenuRepository) {
        this.managerMenuRepository = managerMenuRepository;
        loadResourceDefine();
    }

    // 메뉴 데이터를 로드하여 맵에 저장
    private synchronized void loadResourceDefine() {
        List<ManagerMenu> menus = managerMenuRepository.findAll();
        Map<String, ManagerMenu> newMap = new HashMap<>();

        // 3차 메뉴만 먼저 매핑 (더 구체적인 권한)
        for (ManagerMenu menu : menus) {
            String url = menu.getUrl();

            if (menu.getMenuLevel() == 3 && url != null && !url.isEmpty() && !url.equals("#")) {
                log.info("메뉴 URL 매핑 (3차) - URL: {}, Name: {}", url, menu.getMenuName());

                // 기본 URL 매핑
                newMap.put(url, menu);
                
                // API URL 매핑 (리스트 API와 CRUD API 모두 포함)
                String baseApiUrl = url.endsWith("/") ? url + "api" : url + "/api";
                newMap.put(baseApiUrl, menu);  // 리스트 API
                
                // 상위 경로의 API URL도 매핑 (예: /masterpage_sys/member/api)
                if (url.contains("/")) {
                    String parentUrl = url.substring(0, url.lastIndexOf("/"));
                    String parentApiUrl = parentUrl + "/api";
                    newMap.put(parentApiUrl, menu);  // 상위 경로 API
                    log.debug("API URL 매핑 추가: {} (리스트), {} (상위 리스트)", baseApiUrl, parentApiUrl);
                } else {
                    log.debug("API URL 매핑 추가: {} (리스트)", baseApiUrl);
                }
            }
        }

        // 2차 메뉴는 3차 메뉴에 매핑되지 않은 URL만 매핑
        for (ManagerMenu menu : menus) {
            String url = menu.getUrl();

            if (menu.getMenuLevel() == 2 && url != null && !url.isEmpty() && !url.equals("#")) {
                // 이미 3차 메뉴에 매핑된 URL인지 확인
                if (!newMap.containsKey(url)) {
                    log.info("메뉴 URL 매핑 (2차) - URL: {}, Name: {}", url, menu.getMenuName());

                    // 기본 URL 매핑
                    newMap.put(url, menu);

                    // API URL 매핑
                    String baseApiUrl = url.endsWith("/") ? url + "api" : url + "/api";
                    newMap.put(baseApiUrl, menu);  // 리스트 API
                    
                    log.debug("API URL 매핑 추가: {} (리스트)", baseApiUrl);
                }
            }
        }

        urlToMenuMap = newMap;
        matchCache.clear();  // 캐시 초기화
        log.info("메뉴 URL 매핑 완료 - 총 {} 개의 URL이 등록됨", urlToMenuMap.size());
        urlToMenuMap.forEach((pattern, menu) -> 
            log.debug("URL 패턴: {}, 메뉴: {} (Level: {})", pattern, menu.getMenuName(), menu.getMenuLevel()));
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
        String method = fi.getRequest().getMethod();
        String contextPath = fi.getRequest().getContextPath();
        if (!StringUtils.isEmpty(contextPath)) {
            uri = uri.substring(contextPath.length());
        }

        log.debug("권한 체크 요청 - URI: {}, Method: {}", uri, method);

        // 캐시된 결과가 있으면 반환
        Collection<ConfigAttribute> cached = matchCache.get(uri);
        if (cached != null) {
            return cached;
        }

        // 회원가입 페이지와 로그인 페이지는 명확히 허용
        if ("/member/joinForm".equals(uri) || "/login".equals(uri)) {
            Collection<ConfigAttribute> attrs = SecurityConfig.createList("PERMIT_ALL");
            matchCache.put(uri, attrs);
            return attrs;
        }

        // API 요청인 경우 부모 URL의 권한을 체크
        if (uri.contains("/api")) {
            String baseUri = uri.substring(0, uri.indexOf("/api"));
            ManagerMenu menu = urlToMenuMap.get(baseUri + "/api");
                if (menu != null) {
                    Collection<ConfigAttribute> attrs = List.of(new MenuConfigAttribute(menu));
                    matchCache.put(uri, attrs);
                    return attrs;
            }
        }

        // 일반 URL 매칭
        ManagerMenu menu = urlToMenuMap.get(uri);
        if (menu != null) {
            Collection<ConfigAttribute> attrs = List.of(new MenuConfigAttribute(menu));
            matchCache.put(uri, attrs);
            return attrs;
        }

        // 매칭되는 메뉴가 없으면 기본 허용
        Collection<ConfigAttribute> defaultAttrs = SecurityConfig.createList("PERMIT_ALL");
        matchCache.put(uri, defaultAttrs);
        return defaultAttrs;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public void onApplicationEvent(ManagerMenuChangeEvent event) {
        log.info("ManagerMenu 변경 이벤트 수신, 메뉴 URL 맵을 갱신합니다.");
        loadResourceDefine();
    }
}
