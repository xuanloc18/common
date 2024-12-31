package dev.cxl.iam_service.domain.command;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermissionCommand {

    String roleCode;
    List<String> permissionIds;
}
