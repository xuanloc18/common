package dev.cxl.iam_service.domain.command;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRoleCommand {
    private String userMail;
    private List<String> roleCodes;
}
