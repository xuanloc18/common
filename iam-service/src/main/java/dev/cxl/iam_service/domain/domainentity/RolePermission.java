package dev.cxl.iam_service.domain.domainentity;

import java.util.UUID;

import jakarta.persistence.*;

import dev.cxl.iam_service.application.dto.request.RolePermissionRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermission extends Auditable {
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String roleId;
    String permissionId;
    Boolean deleted = false;

    public RolePermission(RolePermissionRequest rolePermissionRequest) {
        this.id = UUID.randomUUID().toString();
        this.roleId = rolePermissionRequest.getRoleId();
        this.permissionId = rolePermissionRequest.getPermissionId();
        this.deleted = false;
    }
}
