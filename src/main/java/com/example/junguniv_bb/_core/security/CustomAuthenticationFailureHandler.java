package com.example.junguniv_bb._core.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof DisabledException) {
            // 계정 중지 상태일 때의 처리
            setDefaultFailureUrl("/id-stop");
        } else {
            // 그 외의 예외 처리
            setDefaultFailureUrl("/login?error=true");
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
