package dev.cxl.iam_service.infrastructure.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "role_permission")
public class RolePermissionEntity extends AuditableEntity {

    @Id
    @Column(name = "id")
    String id;

    @Column(name = "role_id")
    String roleId;

    @Column(name = "permission_id")
    String permissionId;

    @Column(name = "deleted")
    Boolean deleted = false;
}
