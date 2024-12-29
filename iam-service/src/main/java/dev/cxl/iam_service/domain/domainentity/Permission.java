package dev.cxl.iam_service.domain.domainentity;

import java.util.UUID;

import dev.cxl.iam_service.application.dto.request.PermissionRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends Auditable {

    String id;
    Boolean deleted = false;
    String name;
    String resourceCode;
    String scope;

    public Permission(PermissionRequest permissionRequest) {
        this.id = UUID.randomUUID().toString();
        this.name = permissionRequest.getName();
        this.resourceCode = permissionRequest.getResourceCode();
        this.scope = permissionRequest.getScope();
        this.deleted = false;
    }

    public void delete() {
        this.deleted = true;
    }
}
