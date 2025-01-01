package dev.cxl.iam_service.domain.domainentity;

import java.util.ArrayList;
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
    List<RolePermission> rolePermission=new ArrayList<>();

    public Role(CreateRoleCommand createRoleCommand, List<String> idPerExits) {
        this.id = UUID.randomUUID().toString();
        this.name = createRoleCommand.getName();
        this.code = createRoleCommand.getCode();
        this.description = createRoleCommand.getDescription();
        this.deleted = false;
        this.assignRolePermissions(idPerExits);
    }

    public void assignRolePermissions(List<String> idPerExits) {
        if (idPerExits != null && !idPerExits.isEmpty()) {
            idPerExits.forEach(permission -> {
                RolePermission rolePermissionDomain = new RolePermission(this.id, permission);
                this.rolePermission.add(rolePermissionDomain);
            });
        }
    }

    public void roleAddPermissions(List<String> permissionIds) {
        if (this.rolePermission == null) {
            this.rolePermission = new ArrayList<>();
        }
        List<String> idPerExits =
                this.rolePermission.stream().map(RolePermission::getPermissionId).toList();
        this.getRolePermission().stream()
                .filter(rolePermissionDomain ->
                        permissionIds.contains(rolePermissionDomain.getPermissionId()) && rolePermissionDomain.getDeleted())
                .forEach(RolePermission::setDeleted);

        List<RolePermission> rolePermissions = permissionIds.stream()
                .filter(permissionId -> !idPerExits.contains(permissionId))
                .map(s -> new RolePermission(this.id, s))
                .toList();
        this.rolePermission.addAll(rolePermissions);


    }

    public void roleDeletePermissions(List<String> permissionIds) {
        this.getRolePermission().stream()
                .filter(rolePermissionDomain -> permissionIds.contains(rolePermissionDomain.getPermissionId()))
                .forEach(RolePermission::delete);
    }

    public void setRolePermissions(List<RolePermission> rolePermissionDomains) {
        this.rolePermission = rolePermissionDomains;
    }

    public void delete() {
        this.deleted = true;
    }
}
