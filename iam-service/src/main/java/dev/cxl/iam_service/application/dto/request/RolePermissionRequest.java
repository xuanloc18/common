package dev.cxl.iam_service.application.dto.request;

import jakarta.persistence.*;

import dev.cxl.iam_service.infrastructure.entity.AuditableEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermissionRequest extends AuditableEntity {

    String roleId;
    String permissionId;
}
