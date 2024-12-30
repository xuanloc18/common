package dev.cxl.iam_service.domain.command;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserReplacePassCommand {

    String oldPassword;
    String newPassword;

    @Size(min = 8, message = "PASSWORD_EXCEPTION")
    String confirmPassword;
}
