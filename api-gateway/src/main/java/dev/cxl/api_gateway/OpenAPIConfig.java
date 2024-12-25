package dev.cxl.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class OpenAPIConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r.path("/iam/iam-swagger/v3/api-docs")
                        .and().method(HttpMethod.GET)
                        .uri("lb://iam"))
                .route(r -> r.path("/storage/storage-swagger/v3/api-docs")
                        .and().method(HttpMethod.GET)
                        .uri("lb://storage"))
                .build();
    }
}