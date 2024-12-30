package dev.cxl.iam_service.domain.command;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRoleCommand {
    private String userMail;
    private List<String> roleCodes;
}
