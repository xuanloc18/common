package dev.cxl.iam_service.domain.command;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRoleDeleteCommand {
    String userMail;
    String roleCode;
}
