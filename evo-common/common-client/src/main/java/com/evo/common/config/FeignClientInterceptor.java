package com.evo.common.config;

import com.evo.common.client.iam.IamClient;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

    @Value("${spring.application.client-id}")
    private String client_id;
    @Value("${spring.application.client-secret}")
    private String client_secret;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = getClientToken();
        if (token != null && !token.isEmpty()) {
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }
    private String getClientToken() {

        String tokenUrl = "http://localhost:8088/iam/auth/client-token/{clientId}/{clientSecret}";
        String clientId = client_id;
        String clientSecret = client_secret;
        RestTemplate restTemplate = new RestTemplate();
        try {
            // Call the auth service to get the token
            ResponseEntity<String> response = restTemplate.getForEntity(
                    tokenUrl,
                    String.class,
                    clientId,
                    clientSecret
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody(); // Assuming the token is the plain response body
            } else {
                throw new RuntimeException("Failed to retrieve token. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching client token: " + e.getMessage(), e);
        }
    }


}
