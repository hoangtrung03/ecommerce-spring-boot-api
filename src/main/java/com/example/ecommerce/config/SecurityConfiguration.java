package com.example.ecommerce.config;

import com.example.ecommerce.dto.response.ResultResponse;
import com.example.ecommerce.repository.TokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.List;

import static com.example.ecommerce.entity.Permission.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/api/v1/role/**",
            "/api/v1/users/**",
            "/api/v1/emails/**"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final TokenRepository tokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers(POST, "/api/v1/auth/**").permitAll()
                                .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
                                .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    final String authHeader = request.getHeader("Authorization");

                                    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                                        sendLogoutResponse(response, "Logout failed", HttpStatus.UNAUTHORIZED.value());
                                        return;
                                    }

                                    String jwt = authHeader.substring(7);
                                    var storedToken = tokenRepository.findByToken(jwt).orElse(null);

                                    if (storedToken != null) {
                                        storedToken.setExpired(true);
                                        storedToken.setRevoked(true);
                                        tokenRepository.save(storedToken);
                                        sendLogoutResponse(response, "Logout successful", HttpStatus.OK.value());
                                        SecurityContextHolder.clearContext();
                                    } else {
                                        sendLogoutResponse(response, "Logout failed", HttpStatus.UNAUTHORIZED.value());
                                    }
                                })
                )
        ;

        return http.build();
    }

    private void sendLogoutResponse(HttpServletResponse response, String message, int code) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = generateLogoutResponse(message, code);
        response.getWriter().write(jsonResponse);
        response.setStatus(code);
    }

    private String generateLogoutResponse(String message, Integer code) {
        ResultResponse<String> resultResponse = new ResultResponse<>(HttpStatus.OK.value(), "Logout successful");

        return "{"
                + "\"code\":" + code + ","
                + "\"message\":\"" + message + "\","
                + "\"data\":\"" + resultResponse.getData() + "\""
                + "}";
    }

    /**
     * Creates and configures a CorsConfigurationSource object for handling CORS requests.
     *
     * @return  The CorsConfigurationSource object with the specified CORS settings.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3001"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
