package dev.cxl.iam_service.domain.domainentity;

import java.util.UUID;

import dev.cxl.iam_service.domain.command.PermissionCommand;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends Auditable {

    String id;
    Boolean deleted = false;
    String name;
    String resourceCode;
    String scope;

    public Permission(PermissionCommand permissionCommand) {
        this.id = UUID.randomUUID().toString();
        this.name = permissionCommand.getName();
        this.resourceCode = permissionCommand.getResourceCode();
        this.scope = permissionCommand.getScope();
        this.deleted = false;
    }

    public void delete() {
        this.deleted = true;
    }
}
