package dev.cxl.iam_service.domain.command;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRoleCommand {

    String name;
    String code;
    String description;
    Boolean deleted = false;
    List<String> permissions;
}
