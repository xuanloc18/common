package dev.cxl.iam_service.domain.domainentity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRoleDomain extends AuditableEntityDomain {

    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String userID;
    String roleID;
    Boolean deleted = false;
}
