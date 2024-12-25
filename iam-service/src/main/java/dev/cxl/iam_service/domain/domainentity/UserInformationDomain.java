package dev.cxl.iam_service.domain.domainentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserInformationDomain extends AuditableEntityDomain {
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
