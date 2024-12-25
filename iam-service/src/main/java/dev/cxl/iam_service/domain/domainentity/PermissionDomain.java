package dev.cxl.iam_service.domain.domainentity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionDomain extends AuditableEntityDomain {

    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Boolean deleted = false;
    String name;
    String resourceCode;
    String scope;
}
