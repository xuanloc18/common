package dev.cxl.iam_service.domain.domainentity;

import java.util.List;
import java.util.UUID;

import dev.cxl.iam_service.domain.command.CreateRoleCommand;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends Auditable {
    String id;
    String name;
    String code;
    String description;
    Boolean deleted;
    List<RolePermission> rolePermissionDomains;

    public Role(CreateRoleCommand createRoleCommand) {
        this.id = UUID.randomUUID().toString();
        this.name = createRoleCommand.getName();
        this.code = createRoleCommand.getCode();
        this.description = createRoleCommand.getDescription();
        this.deleted = false;
        this.assignRolePermissions(createRoleCommand);
    }

    public void assignRolePermissions(CreateRoleCommand createRoleCommand) {
        if (createRoleCommand.getPermissions() != null
                && !createRoleCommand.getPermissions().isEmpty()) {
            createRoleCommand.getPermissions().forEach(permission -> {
                RolePermission rolePermissionDomain = new RolePermission(this.id, permission);
                this.rolePermissionDomains.add(rolePermissionDomain);
            });
        }
    }

    public void roleAddPermissions(List<String> permissionIds) {
        permissionIds.forEach(permissionId -> {
            RolePermission rolePermissionDomain = new RolePermission(this.id, permissionId);
            this.rolePermissionDomains.add(rolePermissionDomain);
        });
    }

    public void roleDeletePermissions(List<String> permissionIds) {
        permissionIds.forEach(permissionId -> {
            this.rolePermissionDomains.removeIf(
                    rolePermission -> rolePermission.getPermissionId().equals(permissionId));
        });
    }

    public void setRolePermissions(List<RolePermission> rolePermissionDomains) {
        this.rolePermissionDomains = rolePermissionDomains;
    }

    public void delete() {
        this.deleted = true;
    }
}
