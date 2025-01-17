package dev.cxl.iam_service.infrastructure.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user_roles")
public class UserRoleEntity extends AuditableEntity {

    @Id
    @Column(name = "id")
    String id;

    @Column(name = "user_id")
    String userID;

    @Column(name = "role_id")
    String roleID;

    @Column(name = "deleted")
    Boolean deleted = false;
}
