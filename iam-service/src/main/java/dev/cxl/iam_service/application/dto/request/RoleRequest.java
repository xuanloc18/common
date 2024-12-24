package dev.cxl.iam_service.application.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {

    String name;
    String code;
    String description;
    Boolean deleted = false;
}