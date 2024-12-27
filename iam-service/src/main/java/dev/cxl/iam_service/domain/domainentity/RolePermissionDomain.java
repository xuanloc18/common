package dev.cxl.iam_service.domain.domainentity;

import dev.cxl.iam_service.application.dto.request.RolePermissionRequest;
import dev.cxl.iam_service.infrastructure.entity.RolePermission;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

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

    public RolePermissionDomain(RolePermissionRequest rolePermissionRequest) {
        this.id = UUID.randomUUID().toString();
        this.roleId = rolePermissionRequest.getRoleId();
        this.permissionId = rolePermissionRequest.getPermissionId();
        this.deleted = false;
    }

}
