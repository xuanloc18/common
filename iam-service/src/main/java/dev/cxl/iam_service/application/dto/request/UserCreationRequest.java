package dev.cxl.iam_service.application.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    String userName;

    @Email(message = "EMAIL_EXCEPTION")
    String userMail;

    @Size(min = 8, message = "PASSWORD_EXCEPTION")
    String passWord;

    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String avatar;
    Boolean deleted;
    List<String> roleCode;
}
