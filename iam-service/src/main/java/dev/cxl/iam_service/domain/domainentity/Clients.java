package dev.cxl.iam_service.domain.domainentity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Clients {
    private String clientId;
    private String clientSecret;
}
