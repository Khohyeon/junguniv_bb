package com.example.junguniv_bb._core.security;

import com.example.junguniv_bb._core.permission.CustomAccessDecisionManager;
import com.example.junguniv_bb._core.permission.DynamicSecurityMetadataSource;
import com.example.junguniv_bb.domain.managerauth.service.ManagerAuthService;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenuRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtProvider provider;
    private final ManagerMenuRepository managerMenuRepository;
    private final ManagerAuthService managerAuthService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    public SecurityConfig(JwtProvider provider, ManagerMenuRepository managerMenuRepository,
            ManagerAuthService managerAuthService, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.provider = provider;
        this.managerMenuRepository = managerMenuRepository;
        this.managerAuthService = managerAuthService;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    /**
     * WebSecurityCustomizer를 사용하여 /WEB-INF/** 경로를 Spring Security의 필터링에서 제외
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/WEB-INF/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 페이지별 인가 설정
                .authorizeHttpRequests((auth) -> auth
                        // .requestMatchers("/**").permitAll() // ! (Dev) 개발단계에서 모든 경로에 대한 접근 허용, 배포시에 제거해야함.

                        .requestMatchers(HIDDEN_PATTERNS).denyAll() // 숨겨진 파일 접근 차단
                        .requestMatchers(WHITELIST).permitAll()
                        .requestMatchers(ADMIN).hasAuthority("ADMIN")
                        .requestMatchers(TEACHER).hasAnyAuthority("ADMIN", "TEACHER")
                        .requestMatchers(STUDENT).hasAnyAuthority("ADMIN", "STUDENT")
                        .anyRequest().authenticated())
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(new JwtAuthenticationEntryPoint(), 
                            new AntPathRequestMatcher("/api**"))
                        .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"), 
                            new AntPathRequestMatcher("/**"))
                        .accessDeniedHandler(new JwtAccessDeniedHandler()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .cors(httpSecurityCorsConfigurer -> 
                    httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                
                // 보안 헤더 설정 추가
                .headers(headers -> headers
                    .contentSecurityPolicy(csp -> csp
                            .reportOnly()
                            .policyDirectives(
                                    "default-src 'self'; " +
                                            "script-src 'self' https://code.jquery.com https://cdn.jsdelivr.net https://stackpath.bootstrapcdn.com https://maxcdn.bootstrapcdn.com https://t1.daumcdn.net https://postcode.map.daum.net https://ssl.daumcdn.net https://cdn.tiny.cloud; " +
                                            "style-src 'self' https://cdn.jsdelivr.net https://code.jquery.com https://maxcdn.bootstrapcdn.com https://t1.daumcdn.net https://postcode.map.daum.net https://cdn.tiny.cloud 'unsafe-inline'; " +
                                            "img-src 'self' data: https:; " +
                                            "font-src 'self' https://cdn.jsdelivr.net https://maxcdn.bootstrapcdn.com https://cdn.tiny.cloud data:; " +
                                            "connect-src 'self' https://t1.daumcdn.net https://postcode.map.daum.net https://cdn.tiny.cloud; " +
                                            "frame-src 'self' https://postcode.map.daum.net; " +
                                            "form-action 'self'; " +
                                            "base-uri 'self'; " +
                                            "object-src 'none';"
                            )
                    )
                    .frameOptions(frame -> frame.sameOrigin())
                    .xssProtection(xss -> xss.disable())
                    .contentTypeOptions(withDefaults())
                    .referrerPolicy(referrer -> referrer
                        .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                    .permissionsPolicy(permissions -> permissions
                        .policy("geolocation=(), camera=(), microphone=()"))
                )
                .addFilterBefore(new JWTAuthenticationFilter(provider), 
                    UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(filterSecurityInterceptor(authenticationManager(null)), FilterSecurityInterceptor.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowedOrigins(List.of("http://localhost", "http://localhost:8080", "http://3.35.234.97"));
        configuration.addExposedHeader("Authorization");
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader(JwtProvider.HEADER);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true); // 이중 슬래시 허용
        firewall.setAllowSemicolon(false); // 세미콜론 비허용
        firewall.setAllowUrlEncodedPercent(false); // 인코딩된 퍼센트 문자 비허용
        firewall.setAllowBackSlash(false); // 백슬래시 비허용
        firewall.setAllowUrlEncodedPeriod(false); // 인코딩된 마침표 비허용
        firewall.setAllowUrlEncodedSlash(false); // URL 인코딩된 슬래시 비허용
        firewall.setAllowSemicolon(false); // 세미콜론 비허용
        return firewall;
    }

    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor(
            AuthenticationManager authenticationManager) throws Exception {
        FilterSecurityInterceptor interceptor = new FilterSecurityInterceptor();
        interceptor.setSecurityMetadataSource(dynamicSecurityMetadataSource());
        interceptor.setAccessDecisionManager(customAccessDecisionManager());
        interceptor.setAuthenticationManager(authenticationManager); // 수정된 부분
        return interceptor;
    }

    @Bean
    public DynamicSecurityMetadataSource dynamicSecurityMetadataSource() {
        return new DynamicSecurityMetadataSource(managerMenuRepository);
    }

    @Bean
    public CustomAccessDecisionManager customAccessDecisionManager() {
        return new CustomAccessDecisionManager(managerAuthService);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    /* 스프링 시큐리티 엔드포인트 권한 설정 */
    // TODO BB 프로젝트 권한에 따른 엔드포인트 재설정 필요!!!
    public static final String[] ADMIN = {"/masterpage_sys/**", "masterpage_pro/**"}; // LMS 관리자
    public static final String[] STUDENT = {"/nGsmart/student/**",}; // 학생
    public static final String[] TEACHER = {"/nGsmart/teacher/**",}; // 내용전문가
    public static final String[] COMPANY = {"/nGsmart/student/**",}; // 교강사

    public static final String[] WHITELIST = {
        "/css/**", 
        "/static/**",
        "/include_com/**",
        "/js/**",
        "/images/**",
        "/login", 
        "/access-denied",
        "/join",
        "/id-stop", 
        "/api/v1/auth/idCheck", 
        "/api/v1/auth/join", 
        "/api/v1/auth/login",
        "/api/v1/auth/logout",
        "/api/v1/auth/re-login", 
        "/api/v1/auth/refresh-access-token",
        "/h2-console/**", 
        "/h2-console", 
        "/fro_end",
    };

    // 접근 차단할 숨겨진 파일 패턴(CSP 정책 준수용)
    private static final String[] HIDDEN_PATTERNS = {
        "/.git/**",
        "/.svn/**",
        "/.hg/**",
        "/BitKeeper/**",
        "/.bzr/**",
        "/CVS/**",
        "/.DS_Store",
        "/.hgignore",
        "/.gitignore",
        "/thumbs.db",
        "/.idea/**",
        "/node_modules/**",
        "*/.git/*",
        "*/.svn/*",
        "*/.hg/*",
        "*/BitKeeper/*",
        "*/.bzr/*",
        "*/CVS/*",
        "*/.DS_Store",
        "*/.hgignore",
        "*/.gitignore",
        "*/thumbs.db",
        "*/.idea/*",
        "*/node_modules/*"
    };
}