package dev.cxl.iam_service.domain.domainentity;

import dev.cxl.iam_service.application.dto.request.RoleRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDomain extends AuditableEntityDomain {
    String id;
    String name;
    String code;
    String description;
    Boolean deleted = false;

    public RoleDomain create(RoleRequest roleRequest) {
        return RoleDomain.builder()
                .id(UUID.randomUUID().toString())
                .code(roleRequest.getCode())
                .name(roleRequest.getName())
                .description(roleRequest.getDescription())
                .build();
    }

    public void delete() {
        this.deleted = true;
    }
}
