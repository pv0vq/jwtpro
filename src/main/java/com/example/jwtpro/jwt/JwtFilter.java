package com.example.jwtpro.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilter { // jwt필터설정
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider; // TokenProvider에서 생성된 토큰

    public JwtFilter(TokenProvider tokenProvider) {//필터에 토큰을 주입
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) // 실질적 필터링 담당
            throws IOException, ServletException { //JWT 인증정보를 현재 실행중인 Security Context에 저장하는 역할 수행
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest; // 리퀘스트 정보담기
        String jwt = resolveToken(httpServletRequest); // 리퀘스트에 있는 토큰정보담기
        String requestURI = httpServletRequest.getRequestURI(); //

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // 토큰의 유효성 검사
            Authentication authentication = tokenProvider.getAuthentication(jwt); // 토큰이 정상이면 getAuthentication으로 authentication정보를 받아와서서
             SecurityContextHolder.getContext().setAuthentication(authentication); //ContextHolder 넣어줌
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) { // request header에서 토큰정보를 꺼내오기위한 메소드
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
