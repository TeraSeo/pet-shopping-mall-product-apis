package com.shoppingmall.product.security.filter;

import com.shoppingmall.product.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        boolean isValidToken = jwtTokenProvider.validateToken(accessToken);
        if (accessToken != null && isValidToken) {
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LOGGER.debug("Filter passed");
        }
        else if (refreshToken != null && !isValidToken) {
            LOGGER.debug(refreshToken);
            boolean isValidRefreshToken = jwtTokenProvider.validateToken(refreshToken);
            if (isValidRefreshToken) {
                accessToken = jwtTokenProvider.regenerateAccessToken(refreshToken);
                jwtTokenProvider.setHeaderAccessToken(response, accessToken);
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                LOGGER.debug("token regenerated");
            }
            else {
                LOGGER.debug("not valid refresh token");
            }
        }

        filterChain.doFilter(request, response);
    }
}