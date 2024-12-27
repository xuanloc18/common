package dev.cxl.iam_service.domain.domainentity;

import java.util.List;
import java.util.UUID;


import dev.cxl.iam_service.application.dto.request.RoleRequest;
import dev.cxl.iam_service.infrastructure.entity.RolePermission;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDomain extends AuditableEntityDomain {
    String id;
    String name;
    String code;
    String description;
    Boolean deleted;
    List<RolePermissionDomain> rolePermissionDomains;

    public RoleDomain(RoleRequest roleRequest) {
        this.id = UUID.randomUUID().toString();
        this.name = roleRequest.getName();
        this.code = roleRequest.getCode();
        this.description = roleRequest.getDescription();
        this.deleted = false;
        this.assignRolePermissions(roleRequest);
    }

    public void assignRolePermissions(RoleRequest roleRequest) {
        if (roleRequest.getPermissions() != null
                && !roleRequest.getPermissions().isEmpty()) {
            roleRequest.getPermissions().forEach(permission -> {
                RolePermissionDomain rolePermissionDomain = new RolePermissionDomain();
                rolePermissionDomain.setPermissionId(permission);
                rolePermissionDomain.setRoleId(this.id);
                this.rolePermissionDomains.add(rolePermissionDomain);
            });
        }
    }

    public void delete() {
        this.deleted = true;
    }
}
