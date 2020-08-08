package com.udacity.jwdnd.course1.cloudstorage.config;

import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.catalina.filters.CorsFilter;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
//@EnableRedisHttpSession
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LogoutHandler logoutHandler;

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expiry.ms}")
    private long expiry;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/user/signup").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/signup").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/logout").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/invalidCredentials").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/css/*",
                        "/js/*",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/error.html")
                .and()
                .logout().addLogoutHandler(logoutHandler)
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), secret, expiry))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), secret));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().and().userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "/api/user/invalidCredentials")
        .antMatchers(HttpMethod.GET, "/api/user/logout");
    }
}
