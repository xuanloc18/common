package dev.cxl.iam_service.domain.query;

import com.evo.common.dto.response.SearchRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSearchQuery extends SearchRequest {
    private String userName; // Tìm kiếm theo username
    private String userMail; // Tìm kiếm theo email
    private String firstName;
    private String lastName; // Tìm kiếm theo fullName
}
