package dev.cxl.iam_service.domain.domainentity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryActivityDomain {

    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String userID;
    String activityType;
    String activityName;
    LocalDateTime activityStart;
    String browserID;
}
