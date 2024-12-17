package dev.cxl.iam_service.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateRequest {

    @Size(min = 8, message = "PASSWORD_EXCEPTION")
    String passWord;

    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    Boolean enabled;
    Boolean deleted;
}
