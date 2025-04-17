package com.app.Music_Web.Infrastructure.Config;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.app.Music_Web.Infrastructure.Security.CustomAuthenticationSuccessHandler;
import com.app.Music_Web.Infrastructure.Security.CustomLogoutSuccessHandler;
import com.app.Music_Web.Infrastructure.Security.JwtRequestFilter;

@Configuration
public class SecurityConfig {
    private JwtRequestFilter jwtRequestFilter;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;

    public SecurityConfig(
        JwtRequestFilter jwtRequestFilter,
        CustomLogoutSuccessHandler logoutSuccessHandler
        ) {
        this.jwtRequestFilter=jwtRequestFilter;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        CustomAuthenticationSuccessHandler successHandler) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/roles/**","/api/permissions/**").hasAuthority("SYSTEM_MANAGEMENT")
                .requestMatchers("/api/**","/ws/**", "/swagger-ui/**", "/v3/api-docs/**",
                                "/api/auth/login", "/api/auth/login/google").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .redirectionEndpoint(redirection -> redirection
                    .baseUri("/api/auth/login/google") // Xử lý callback từ Google
                )
                .successHandler(successHandler)
                .failureUrl("/api/auth/login?error=true")
                .userInfoEndpoint(userInfo -> userInfo
                    .oidcUserService(oidcUserService()) // Lấy thông tin người dùng từ Google
                )
            )
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll()
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Cho phép Angular
        configuration.setAllowedOrigins(List.of("http://localhost:4200","https://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }




}
