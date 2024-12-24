package dev.cxl.iam_service.application.dto.identity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Logout {
    String client_id;
    String client_secret;
    String refresh_token;
}
