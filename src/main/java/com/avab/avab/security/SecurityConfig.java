package com.avab.avab.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.avab.avab.security.filter.AuthExceptionHandlingFilter;
import com.avab.avab.security.filter.DisabledUserFilter;
import com.avab.avab.security.filter.JwtRequestFilter;
import com.avab.avab.security.handler.JwtAccessDeniedHandler;
import com.avab.avab.security.handler.JwtAuthenticationEntryPoint;
import com.avab.avab.security.handler.SwaggerLoginSuccessHandler;
import com.avab.avab.utils.EnvironmentHelper;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final SwaggerLoginSuccessHandler swaggerLoginSuccessHandler;

    private final AuthExceptionHandlingFilter authExceptionHandlingFilter;
    private final DisabledUserFilter disabledUserFilter;

    @Value("${springdoc.swagger-ui.authentication.username}")
    private String swaggerUsername;

    @Value("${springdoc.swagger-ui.authentication.password}")
    private String swaggerPassword;

    private final String[] allowedUrls = {
        "/api/auth/login/test",
        "/health",
        "/error",
        "/api/recreations/popular",
        "/api/recreations/search",
        "/api/recreations/{recreationId}",
        "/api/recreations/{recreationId}/related/flows",
        "/api/recreations/{recreationId}/related/recreations",
        "/api/recreations/popular",
        "/api/auth/login/kakao",
        "/api/auth/login/kakao/local",
        "/api/auth/refresh",
        "/api/users/scrap",
        "/api/recreations/recommended",
        "/api/flows/recommended",
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http, Environment environment, EnvironmentManager environmentManager)
            throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.headers(
                headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.sessionManagement(
                sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(
                (configurer ->
                        configurer
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)));

        http.authorizeHttpRequests(
                (authorize) ->
                        authorize
                                .requestMatchers(allowedUrls)
                                .permitAll()
                                .requestMatchers(
                                        HttpMethod.GET, "/api/recreations/{recreationId}/reviews")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/flows")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/recreations")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/flows/{flowId}")
                                .permitAll()
                                .anyRequest()
                                .authenticated());

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authExceptionHandlingFilter, JwtRequestFilter.class)
                .addFilterAfter(disabledUserFilter, JwtRequestFilter.class);

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain swaggerFilterChain(
            HttpSecurity http, EnvironmentHelper environmentHelper) throws Exception {
        if (environmentHelper.isLocal()) {
            return http.build();
        }

        http.securityMatcher(
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs",
                        "/login")
                .formLogin(
                        (formLogin) ->
                                formLogin
                                        .successHandler(swaggerLoginSuccessHandler)
                                        .defaultSuccessUrl("/swagger-ui/index.html"))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());

        return http.build();
    }

    @Bean
    @Order(1)
    public UserDetailsService swaggerUserDetailsService() {
        UserDetails swaggerUserDetails =
                User.builder()
                        .username(swaggerUsername)
                        .password(passwordEncoder().encode(swaggerPassword))
                        .roles("DEVELOPER")
                        .build();

        return new InMemoryUserDetailsManager(swaggerUserDetails);
    }
}
