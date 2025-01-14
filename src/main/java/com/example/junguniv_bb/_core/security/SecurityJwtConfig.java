package com.example.junguniv_bb._core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class SecurityJwtConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtProvider tokenProvider;

    @Override
    public void configure(HttpSecurity http){
        JWTAuthenticationFilter securityFilter = new JWTAuthenticationFilter(tokenProvider);
        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
    }

}