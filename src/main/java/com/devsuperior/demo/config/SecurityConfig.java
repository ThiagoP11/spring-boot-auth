package com.devsuperior.demo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Profile("test")
    @Order(1)
    SecurityFilterChain h2SecurityFilterChain(HttpSecurity http) throws Exception {

        http.securityMatcher(PathRequest.toH2Console()).csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*desabilitando a proteção contra ataques do tipo csrf, que é um ataque quando vc tem uma aplicação
          que grava dados na seção, mas como nosso back-end é usando api rest e o mesmo não guarda estado da seção
          então não nos preocupamos com isso.
         */
        http.csrf(AbstractHttpConfigurer::disable);
        //permissão para os meus endpoints
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

}

