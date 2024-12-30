package dev.cxl.iam_service.domain.command;

import java.time.LocalDate;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationCommand {

    String userName;
    String userMail;
    String passWord;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String avatar;
    Boolean deleted;
    List<String> roleCode;}
