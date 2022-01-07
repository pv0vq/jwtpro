package com.example.jwtpro.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> { // SecurityConfigurerAdapter상속받고


    private TokenProvider tokenProvider;

    public JwtSecurityConfig(TokenProvider tokenProvider) { //tokenProvider등록
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) { // JwtFilter 등록
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
