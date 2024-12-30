package com.evo.common.webapp.security;

import com.evo.common.webapp.config.ActionLogFilter;
import com.evo.common.webapp.config.JwtProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Slf4j
@EnableWebSecurity
@EnableFeignClients(basePackages = {"com.evo.common.client"})
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class HttpSecurityConfiguration {

    private final ActionLogFilter actionLogFilter;
    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final ForbiddenTokenFilter forbiddenTokenFilter;
    private final JwtProperties jwtProperties;
    private final String[] SWAGGER_ENDPOINT = {
            "/swagger-ui.html","/iam-swagger/**","/iam-swagger/**","/storage-swagger/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui/index.html","users/confirm"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/health").permitAll()
                                .requestMatchers(HttpMethod.POST,"/users").permitAll()
                                .requestMatchers("/api/certificate/.well-known/jwks.json").permitAll()
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/api/authenticate/**").permitAll()
                                .requestMatchers(SWAGGER_ENDPOINT).permitAll()
                                .anyRequest().authenticated()
                );
               http.oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationManagerResolver(this.jwkResolver(this.jwtProperties)));
        http.oauth2ResourceServer(oauth2 -> oauth2.authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        http.addFilterAfter(this.forbiddenTokenFilter, BearerTokenAuthenticationFilter.class);
        http.addFilterAfter(this.customAuthenticationFilter, BearerTokenAuthenticationFilter.class);
        http.addFilterAfter(this.actionLogFilter, BearerTokenAuthenticationFilter.class);
        // @formatter:on
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthenticationManagerResolver<HttpServletRequest> jwkResolver(JwtProperties jwtProperties) {
        return new JwkAuthenticationManagerResolver(jwtProperties);
    }
        @Bean
        public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("*")); // Thay bằng domain front-end
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
    @Bean
    CustomPermissionEvaluator customPermissionEvaluator() {
        return new CustomPermissionEvaluator();
    }
    // chỉ ra rằng Spring Security sử dụng CustomPermissionEvaluator khi xử lý các bijou thức như @PreAuthoriz
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(customPermissionEvaluator());
        return expressionHandler;
    }

}
