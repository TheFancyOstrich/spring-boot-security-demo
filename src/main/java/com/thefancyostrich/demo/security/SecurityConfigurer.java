package com.thefancyostrich.demo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Required for annotation based config
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Order here matters. Check the http request in order of all antMatchers.
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/user/**").permitAll()// Whitelist wo auth
                // These are if you dont want to annotate:
                // .antMatchers(HttpMethod.GET,
                // "/user/**").hasAuthority(TEST_USER.getPermission())
                // .antMatchers(HttpMethod.GET,
                // "/admin/**").hasAuthority(TEST_ADMIN.getPermission())// Authority/permission
                // .antMatchers(HttpMethod.GET,
                // "/mod/**").hasAuthority(TEST_MODERATOR.getPermission())
                .anyRequest()
                .authenticated()
                .and().httpBasic(); // Basic auth Require username+pass at each request.
    }
}
