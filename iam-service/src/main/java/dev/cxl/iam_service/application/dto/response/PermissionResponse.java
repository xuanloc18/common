package dev.cxl.iam_service.application.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {
    String id;
    Boolean deleted;
    String name;
    String resourceCode;
    String scope;
}
