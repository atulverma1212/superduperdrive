package com.udacity.jwdnd.course1.cloudstorage.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

import static com.udacity.jwdnd.course1.cloudstorage.config.SecurityConstants.HEADER;
import static com.udacity.jwdnd.course1.cloudstorage.config.SecurityConstants.TOKEN_PREFIX;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger LOGGER = Logger.getLogger(JWTAuthenticationFilter.class.getName());

    private AuthenticationManager authenticationManager;

    private final long EXPIRATION_TIME;

    private final String SECRET;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   String secret, long expiry) {
        this.authenticationManager = authenticationManager;
        this.EXPIRATION_TIME = expiry;
        this.SECRET = secret;
    }

    /**
     * parses the user's credentials and issue them to the {@link AuthenticationManager}.
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password + username,
                            new ArrayList<>()
                    )
            );

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        LOGGER.info("Authentication successful");

        String jwt = JWT.create()
                .withSubject(getSubject(authResult))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
        response.addHeader(HEADER, TOKEN_PREFIX + jwt);
        SecurityContextHolder.getContext().setAuthentication(authResult);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute("user", getSubject(authResult));
        }
        response.sendRedirect("/api/user/home");
    }

    private static String getSubject(Authentication authResult) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        return user.getUsername();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        super.unsuccessfulAuthentication(request, response, failed);
        LOGGER.severe("Authentication failed HttpStatus 403");
        response.sendRedirect("/api/user/invalidCredentials");
    }
}