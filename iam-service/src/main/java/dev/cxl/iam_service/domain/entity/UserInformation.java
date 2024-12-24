package dev.cxl.iam_service.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserInformation extends AuditableEntity {
    @Id
    private String username;

    private String fullName;
    private LocalDate dateOfBirth;
    private String streetName;
    private String ward;
    private String district;
    private String province;
    private Integer yearsOfExperience;
    private Boolean deleted;
    private String fileID;
}
