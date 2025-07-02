package com.betacom.app.infrastructure.security;

import com.betacom.app.service.JwtService;
import com.betacom.app.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = request.getHeader(this.tokenHeader);
        if (request.getParameter("token") != null) {
            authToken = request.getParameter("token");
        }
        if(authToken != null){
            UUID userIdFromToken = jwtService.getUserIdFromToken(authToken);
            if (userIdFromToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (!jwtService.isTokenValid(authToken)) {
                    throw new RuntimeException("Token is not valid");
                }
                if(jwtService.isTokenExpired(authToken)){
                    throw new RuntimeException("Token is expired");
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userIdFromToken, null, Collections.emptyList());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }


}
