package com.avab.avab.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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

    @Value("${springdoc.swagger-ui.authentication.username:}")
    private String SWAGGER_USERNAME;

    @Value("${springdoc.swagger-ui.authentication.password:}")
    private String SWAGGER_PASSWORD;

    @Value("${management.security.http-basic.username}")
    private String ACTUATOR_USERNAME;

    @Value("${management.security.http-basic.password}")
    private String ACTUATOR_PASSWORD;

    @Value("${management.endpoints.web.base-path}")
    private String ACTUATOR_BASE_PATH;

    private final List<String> ALLOWED_APIS =
            List.of(
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
                    "/api/flows/recommended");

    private final List<String> SWAGGER_URLS =
            List.of("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/v3/api-docs");

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
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
                                .requestMatchers(ALLOWED_APIS.toArray(String[]::new))
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
    @Order(0)
    public SecurityFilterChain swaggerFilterChain(
            HttpSecurity http, EnvironmentHelper environmentHelper) throws Exception {
        if (environmentHelper.isLocal()) {
            http.securityMatcher(SWAGGER_URLS.toArray(String[]::new))
                    .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll());

            return http.build();
        }

        List<String> securedUrls = new ArrayList<>(SWAGGER_URLS);
        securedUrls.add("/login");

        http.securityMatcher(securedUrls.toArray(String[]::new))
                .formLogin(
                        (formLogin) ->
                                formLogin
                                        .successHandler(swaggerLoginSuccessHandler)
                                        .defaultSuccessUrl("/swagger-ui/index.html"))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());

        return http.build();
    }

    @Bean
    @Order(0)
    public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(ACTUATOR_BASE_PATH + "/**")
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public UserDetailsService inMemoryDevelopers(EnvironmentHelper environmentHelper) {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

        if (environmentHelper.isDev() || environmentHelper.isProd()) {
            UserDetails swaggerUserDetails =
                    User.builder()
                            .username(SWAGGER_USERNAME)
                            .password(passwordEncoder().encode(SWAGGER_PASSWORD))
                            .roles("SWAGGER")
                            .build();

            inMemoryUserDetailsManager.createUser(swaggerUserDetails);
        }

        UserDetails actuatorUserDetails =
                User.builder()
                        .username(ACTUATOR_USERNAME)
                        .password(passwordEncoder().encode(ACTUATOR_PASSWORD))
                        .roles("ACTUATOR")
                        .build();

        inMemoryUserDetailsManager.createUser(actuatorUserDetails);

        return inMemoryUserDetailsManager;
    }
}
