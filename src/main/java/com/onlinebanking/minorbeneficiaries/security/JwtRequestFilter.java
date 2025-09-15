package com.onlinebanking.minorbeneficiaries.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${jwt.validation.api.url}")
    private String jwtValidationApiUrl;

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    public JwtRequestFilter(JwtUtil jwtUtil, RestTemplate restTemplate) {
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            try {
                Map<String, Object> validationResponse = restTemplate.getForObject(
                        jwtValidationApiUrl + "?token=" + jwt, Map.class);

                boolean isValid = "Token valid".equals(validationResponse.get("message"))
                        && jwtUtil.validateToken(jwt);

                if (isValid) {
                    Jws<Claims> jws = jwtUtil.getClaims(jwt);
                    Claims claims = jws.getBody();
                    String subject = claims.getSubject();

                    if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        String role = claims.get("role", String.class);
                        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                        if (role != null) {
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                        }

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                new User(subject, "", authorities),
                                null,
                                authorities);

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        request.setAttribute("jwt_claims", claims);
                        request.setAttribute("user_id", claims.get("username", String.class));
                        request.setAttribute("user_role", role);
                        request.setAttribute("customerId", subject);
                        request.setAttribute("email", claims.get("email", String.class));
                    }
                }

            } catch (Exception e) {
                logger.error("JWT validation failed", e);
            }
        }

        chain.doFilter(request, response);
    }
}