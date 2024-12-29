package dev.cxl.iam_service.domain.domainentity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryActivity {

    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String userID;
    String activityType;
    String activityName;
    LocalDateTime activityStart;
    String browserID;
}
