package dev.cxl.iam_service.application.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionRequest {

    Boolean deleted = false;
    String name;
    String resourceCode;
    String scope;
}
