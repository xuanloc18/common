package com.evo.common.client.iam;

import com.evo.common.UserAuthority;
import com.evo.common.config.FeignClientConfiguration;
import com.evo.common.dto.response.BasedResponse;
import com.evo.common.dto.response.Response;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        url = "http://localhost:8088/api/iam",
        name = "iam",
        contextId = "common-iam",
        configuration = FeignClientConfiguration.class,
        fallbackFactory = IamClientFallback.class)
public interface IamClient {
    @GetMapping("/auth/{userId}/authorities-by-userid")
    @LoadBalanced
    Response<UserAuthority> getUserAuthority(@PathVariable UUID userId);

    @GetMapping("/auth/{clientId}/authorities-by-clientId")
    @LoadBalanced
    BasedResponse<UserAuthority> getClientAuthority(@PathVariable String clientId);

    @GetMapping("/api/users/{username}/authorities-by-username")
    @LoadBalanced
    Response<UserAuthority> getUserAuthority(@PathVariable String username);

    @GetMapping("/client-token/{clientId}/{clientSecret}")
    @LoadBalanced
    BasedResponse<String> getClientToken(@PathVariable String clientId, @PathVariable String clientSecret);


}
