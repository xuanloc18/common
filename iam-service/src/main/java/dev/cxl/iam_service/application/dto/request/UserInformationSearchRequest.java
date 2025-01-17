package dev.cxl.iam_service.application.dto.request;

import java.time.LocalDate;

import com.evo.common.dto.response.SearchRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInformationSearchRequest extends SearchRequest {
    private String username;

    private String fullName;
    private LocalDate dateOfBirth;
    private String streetName;
    private String ward;
    private String district;
    private String province;
    private Integer yearsOfExperience;
    private Boolean deleted;
}
