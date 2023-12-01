package com.kjeldsen.match.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        final String email;

        if (request.getCookies() == null) {
            filterChain.doFilter(request, response);
            return;
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("jwt")) {
                token = cookie.getValue();
                break;
            }
        }
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            email = tokenService.extractUserName(token);
        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }
        boolean isAuthenticated = SecurityContextHolder.getContext().getAuthentication() != null;
        if (StringUtils.isNotEmpty(email) && !isAuthenticated) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (tokenService.isTokenValid(token, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                var authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }
}
