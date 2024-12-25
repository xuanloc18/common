package dev.cxl.iam_service.domain.domainentity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermissionDomain extends AuditableEntityDomain {
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String roleId;
    String permissionId;
    Boolean deleted = false;
}
