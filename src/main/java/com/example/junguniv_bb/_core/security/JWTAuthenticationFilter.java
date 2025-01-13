package com.example.junguniv_bb._core.security;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.junguniv_bb._core.util.RequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// JWT를 통한 인증 처리 필터
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    final JwtProvider provider;

    public JWTAuthenticationFilter(JwtProvider provider) {
        this.provider = provider;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        String jwt = resolveToken(request);

        if (StringUtils.hasText(jwt)) {
            try {
                if (!provider.validateToken(jwt)) {
                    handleTokenError(request, response);
                    return;
                }

                Authentication authentication = provider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
            } catch (TokenExpiredException e) {
                handleTokenExpired(request, response);
                return;
            } catch (SignatureVerificationException e) {
                handleTokenError(request, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /* 토큰 만료 시 처리 */
    private void handleTokenExpired(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        if (RequestUtils.isApiRequest(request)) {
            // API 요청인 경우 401 응답
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"토큰이 만료되었습니다.\",\"code\":\"TOKEN_EXPIRED\"}");
        } else {
            // 웹 요청인 경우 로그인 페이지로 리다이렉트
            response.sendRedirect("/login");
        }
    }

    /* 토큰 유효성 검사 실패 시 처리 */
    private void handleTokenError(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        if (RequestUtils.isApiRequest(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"유효하지 않은 토큰입니다.\",\"code\":\"INVALID_TOKEN\"}");
        } else {
            response.sendRedirect("/login?error=true");
        }
    }

    private String resolveToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute("accessToken");
        }
        return null;
    }
}

