package dev.cxl.iam_service.domain.domainentity;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermission extends Auditable {
    String id;

    String roleId;
    String permissionId;
    Boolean deleted;

    public RolePermission(String roleId, String permissionId) {
        this.id = UUID.randomUUID().toString();
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.deleted = false;
    }

    public void delete() {
        this.deleted = true;
    }

    public void setDeleted() {
        this.deleted = false;
    }
}
