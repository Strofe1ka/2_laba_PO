package com.example.demo.config;

import com.example.demo.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF: CookieCsrfTokenRepository, /register без токена (публичная регистрация)
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/register", "/debug/**"))
            .authorizeHttpRequests(auth -> auth
                // OPTIONS (CORS preflight) — без авторизации, иначе Postman получает 401
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Регистрация и отладка — без авторизации
                .requestMatchers("/register").permitAll()
                .requestMatchers("/debug/secure-test", "/debug/secure-post", "/debug/create-car", "/debug/create-user", "/debug/users", "/debug/users/**", "/debug/start-ride", "/debug/rides", "/debug/rides/**").authenticated()
                .requestMatchers("/debug/**").permitAll()
                // Пользователи — только ADMIN
                .requestMatchers("/users/**").hasRole("ADMIN")
                // Автомобили — временно все операции для authenticated (для отладки Postman)
                .requestMatchers("/cars", "/cars/**").authenticated()
                // Поездки и платежи — USER и ADMIN
                .requestMatchers("/rides/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/payments/**").hasAnyRole("USER", "ADMIN")
                // Остальное — требуется аутентификация
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {})
            .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
