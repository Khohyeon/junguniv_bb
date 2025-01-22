// package com.example.junguniv_bb._core.permission;
//
// import com.example.junguniv_bb.domain.managerMenu.model.ManagerMenu;
// import com.example.junguniv_bb.domain.managerMenu.model.ManagerMenuRepository;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.context.ApplicationListener;
// import org.springframework.security.access.ConfigAttribute;
// import org.springframework.security.access.SecurityConfig;
// import org.springframework.security.web.FilterInvocation;
// import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
// import org.springframework.stereotype.Component;
// import org.springframework.util.AntPathMatcher;
// import org.springframework.util.StringUtils;
//
// import java.util.Collection;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;
//
// @Component
// @Slf4j
// public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, ApplicationListener<ManagerMenuChangeEvent> {
//
//     private final ManagerMenuRepository managerMenuRepository;
//     private final AntPathMatcher antPathMatcher = new AntPathMatcher();
//     private volatile Map<String, ManagerMenu> urlToMenuMap = new ConcurrentHashMap<>();
//
//     private static final String API_PATTERN = "/nGmaster/**/api/**";
//     private static final String WEB_PATTERN = "/**";
//
//     public DynamicSecurityMetadataSource(ManagerMenuRepository managerMenuRepository) {
//         this.managerMenuRepository = managerMenuRepository;
//         loadResourceDefine();
//     }
//
//     // 메뉴 데이터를 로드하여 맵에 저장
//     private synchronized void loadResourceDefine() {
//         List<ManagerMenu> menus = managerMenuRepository.findAll();
//         Map<String, ManagerMenu> newMap = new HashMap<>();
//         for (ManagerMenu menu : menus) {
//             newMap.put(menu.getMenuUrl(), menu);
//         }
//         urlToMenuMap = newMap;
//         // log.info("DynamicSecurityMetadataSource 메뉴 URL 맵 갱신 완료: {}", urlToMenuMap.keySet());
//     }
//
//     @Override
//     public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
//         FilterInvocation fi = (FilterInvocation) object;
//         String uri = fi.getRequest().getRequestURI();
//         String contextPath = fi.getRequest().getContextPath();
//         if (!StringUtils.isEmpty(contextPath)) {
//             uri = uri.substring(contextPath.length());
//         }
//
//         // 회원가입 페이지와 로그인 페이지는 명확히 허용
//         if ("/member/joinForm".equals(uri) || "/login".equals(uri)) {
//             return SecurityConfig.createList("PERMIT_ALL");
//         }
//
//         for (Map.Entry<String, ManagerMenu> entry : urlToMenuMap.entrySet()) {
//             String menuUrl = entry.getKey();
//             if (antPathMatcher.match(menuUrl, uri)) {
//                 ManagerMenu menu = entry.getValue();
//                 return List.of(new MenuConfigAttribute(menu));
//             }
//         }
//
//         // 매칭되는 메뉴가 없을 경우 접근 허용
//         return SecurityConfig.createList("PERMIT_ALL");
//     }
//
//     @Override
//     public Collection<ConfigAttribute> getAllConfigAttributes() {
//         // 모든 ConfigAttribute를 반환하거나 null을 반환할 수 있습니다.
//         // 여기서는 null을 반환합니다.
//         return null;
//     }
//
//     @Override
//     public boolean supports(Class<?> clazz) {
//         // 이 SecurityMetadataSource가 주어진 클래스 타입을 지원하는지 여부를 반환
//         return FilterInvocation.class.isAssignableFrom(clazz);
//     }
//
//     @Override
//     public void onApplicationEvent(ManagerMenuChangeEvent event) {
//         log.info("ManagerMenu 변경 이벤트 수신, 메뉴 URL 맵을 갱신합니다.");
//         loadResourceDefine();
//     }
// }
