package dev.cxl.iam_service.domain.command;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateCommand {
    String passWord;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    Boolean enabled;
    Boolean deleted;
}
