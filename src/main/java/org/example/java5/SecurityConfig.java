package org.example.java5;

import org.example.java5.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter, CustomUserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/api/admin/sanpham/**").hasRole("ADMIN")
                        .requestMatchers("/api/sanPhamND/**").hasRole("USER")

                        .requestMatchers("/api/admin/**").permitAll()

                        .requestMatchers("/auth/api/**").permitAll()  // Cho phép tất cả các yêu cầu đến API đăng nhập
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Chỉ admin có quyền truy cập
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")  // Người dùng và admin có quyền truy cập
                        .anyRequest().authenticated()  // Các yêu cầu còn lại phải được xác thực
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  // Thêm JWT filter vào trước khi xử lý xác thực mặc định

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
