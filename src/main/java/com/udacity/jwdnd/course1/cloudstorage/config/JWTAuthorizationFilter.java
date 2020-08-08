package com.udacity.jwdnd.course1.cloudstorage.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import static com.udacity.jwdnd.course1.cloudstorage.config.SecurityConstants.HEADER;
import static com.udacity.jwdnd.course1.cloudstorage.config.SecurityConstants.TOKEN_PREFIX;

/**
 * extended the {@link BasicAuthenticationFilter} to make Spring replace it in the filter chain with our custom implementation
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private String SECRET;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, String secret) {
        super(authenticationManager);
        this.SECRET = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        if (authentication1 == null || !authentication1.isAuthenticated()) {
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(request, response);
    }
}
