package com.novelis.elibrary.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll() // public endpoints
                        .requestMatchers("/api/**").authenticated()     // secure endpoints
                        .anyRequest().permitAll()
                )

                .httpBasic(Customizer.withDefaults()); // Enable Basic Auth (ApiDog/Postman)

        return http.build();
    }
}
