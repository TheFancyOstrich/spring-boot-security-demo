package com.thefancyostrich.demo.security;

import static com.thefancyostrich.demo.users.UserRole.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Required for annotation based config
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfigurer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Order here matters. Check the http request in order of all antMatchers.
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/hello").permitAll()// Whitelist wo auth
                // These are if you dont want to annotate:
                // .antMatchers(HttpMethod.GET,
                // "/user/**").hasAuthority(TEST_USER.getPermission())
                // .antMatchers(HttpMethod.GET,
                // "/admin/**").hasAuthority(TEST_ADMIN.getPermission())// Authority/permission
                // .antMatchers(HttpMethod.GET,
                // "/mod/**").hasAuthority(TEST_MODERATOR.getPermission())
                .anyRequest()
                .authenticated()
                .and().formLogin();

        // .httpBasic(); //Basic auth Require username+pass at each request.
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails user = User.builder().username("Erik").password(passwordEncoder.encode("password"))
                .authorities(ADMIN.getGrantedAuthorities())
                .build();
        UserDetails user2 = User.builder().username("Inte Erik").password(passwordEncoder.encode("password"))
                .authorities(USER.getGrantedAuthorities())
                .build();
        return new InMemoryUserDetailsManager(user, user2);
    }
}
