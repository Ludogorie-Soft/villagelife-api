package com.example.ludogorieSoft.village.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .requireCsrfProtectionMatcher(new AndRequestMatcher(
                        CsrfFilter.DEFAULT_CSRF_MATCHER,
                        new RequestHeaderRequestMatcher(HttpHeaders.COOKIE)))
                .and()
                .cors().disable()
                .authorizeHttpRequests()
                .antMatchers("/api/v1/admins/**", "/api/v1/auth/register", "/api/v1/villageImages/resume/{id}", "/api/v1/villageImages/reject/{id}", "/api/v1/villageImages/deleted/with-base64/village/{villageId}", "/api/v1/villageImages/with-base64/village/{villageId}", "/api/v1/villageImages/admin-upload")
                .authenticated()
                .and()
                .authorizeHttpRequests()
                .anyRequest()
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint);

        return http.build();
    }
}
