package com.project.shopapp.config;

import com.project.shopapp.filters.JwtTokenFilter;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;
    // Lọc request gửi đến
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(
                                String.format("%s/users/register", apiPrefix),
                                String.format("%s/users/login", apiPrefix)
                            )
                            .permitAll()
                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/categories**", apiPrefix)
                            ).permitAll()
                            .requestMatchers(
                                    HttpMethod.POST, String.format("%s/categories/**", apiPrefix)
                            ).hasRole(Role.ADMIN)
                            .requestMatchers(
                                    HttpMethod.PUT, String.format("%s/categories/**", apiPrefix)
                            ).hasRole(Role.ADMIN)

                            .requestMatchers(
                                    HttpMethod.DELETE, String.format("%s/categories/**", apiPrefix)
                            ).hasRole(Role.ADMIN)
                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/products**", apiPrefix)
                            ).permitAll()
                            .requestMatchers(
                                    HttpMethod.POST, String.format("%s/products**", apiPrefix)
                            ).hasRole(Role.ADMIN)
                            .requestMatchers(
                                    HttpMethod.PUT, String.format("%s/products/**", apiPrefix)
                            ).hasRole(Role.ADMIN)
                            .requestMatchers(
                                    HttpMethod.DELETE, String.format("%s/products/**", apiPrefix)
                            ).hasRole(Role.ADMIN)

                            .requestMatchers(
                                    HttpMethod.POST, String.format("%s/products/upload/**", apiPrefix)
                            ).hasRole(Role.ADMIN)

                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/products/images/**", apiPrefix)
                            ).permitAll()

                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/orders**", apiPrefix)
                            ).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(
                                    HttpMethod.POST, String.format("%s/orders/**", apiPrefix)
                            ).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(
                                    HttpMethod.PUT, String.format("%s/orders/**", apiPrefix)
                            ).hasRole(Role.ADMIN)
                            .requestMatchers(
                                    HttpMethod.DELETE, String.format("%s/orders/**", apiPrefix)
                            ).hasRole(Role.ADMIN)
                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/roles**", apiPrefix)
                            ).permitAll()
                            .anyRequest()
                            .authenticated();

                })
                .csrf(AbstractHttpConfigurer::disable)
                .cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowedHeaders(List.of("Authorization", "x-auth-token", "Content-Type"));
                        corsConfiguration.setAllowedOrigins(List.of("*"));
                        corsConfiguration.setAllowedMethods(
                                List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE")
                        );
                        corsConfiguration.setExposedHeaders(List.of("x-auth-token"));   // Cấu hình header mà client có thể nhìn thấy sau khi nhận phản hồi từ server
                        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                        source.registerCorsConfiguration("/**", corsConfiguration); // Mọi url đều được áp dụng
                        httpSecurityCorsConfigurer.configurationSource(source);
                    }
                })
                .build();
    }
}
