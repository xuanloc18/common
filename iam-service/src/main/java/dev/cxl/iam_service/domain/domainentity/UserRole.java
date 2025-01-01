package dev.cxl.iam_service.domain.domainentity;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRole extends Auditable {

    String id;
    String userID;
    String roleID;
    Boolean deleted = false;

    public UserRole(String userID, String roleID) {
        this.id = UUID.randomUUID().toString();
        this.userID = userID;
        this.roleID = roleID;
        this.deleted = false;
    }

    public void setDeleted() {
        this.deleted = false;
    }

    public void delete() {
        this.deleted = true;
    }
}
