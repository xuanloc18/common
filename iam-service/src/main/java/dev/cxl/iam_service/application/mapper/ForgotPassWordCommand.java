package dev.cxl.iam_service.application.mapper;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPassWordCommand {
    String token;
    String newPass;
}
