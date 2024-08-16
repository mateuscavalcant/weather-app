package com.weather_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Desabilita a proteção CSRF (opcional)
            .authorizeHttpRequests()
            .requestMatchers("/weather").permitAll()  // Permite acesso ao endpoint /weather sem autenticação
            .anyRequest().authenticated();  // Exige autenticação para outras rotas

        return http.build();
    }
}
