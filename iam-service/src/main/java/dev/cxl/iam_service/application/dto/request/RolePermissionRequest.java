package dev.cxl.iam_service.application.dto.request;

import dev.cxl.iam_service.infrastructure.entity.AuditableEntity;
import jakarta.persistence.*;
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
