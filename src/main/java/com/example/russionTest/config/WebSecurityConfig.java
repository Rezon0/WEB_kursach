package com.example.russionTest.config;

import com.example.russionTest.Services.UserService;
import com.example.russionTest.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests((requests) -> requests
                        //.requestMatchers("/successfulRegistrationUser").permitAll()
                        .requestMatchers("/adminForm/**").hasRole("ADMIN")
                        .requestMatchers("/studentForm/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return username -> {
            User user = userService.getUserByUserName(username);
            //userService.encode(username);
            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password(user.getPassword())
                    .authorities(userService.getAuthorityByusername(username)).build();
        };
    }
}