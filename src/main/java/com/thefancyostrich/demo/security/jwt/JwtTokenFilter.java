package com.thefancyostrich.demo.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thefancyostrich.demo.exceptions.ToHttpException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * The filter configured in spring security that checks the existence and
 * validity of JWT tokens.
 */
public class JwtTokenFilter extends OncePerRequestFilter {
    private JwtTokenProvider provider;

    public JwtTokenFilter(JwtTokenProvider provider) {
        this.provider = provider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = provider.extractToken(request);
        try {
            boolean valid = (token != null && provider.validateToken(token));
            if (valid) {
                Authentication auth = provider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);

            }
        } catch (ToHttpException ex) {
            SecurityContextHolder.clearContext();
            response.sendError(ex.getHttpStatus().value(), ex.getMessage());
            return;

        }

        filterChain.doFilter(request, response);

    }
}
