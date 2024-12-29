package dev.cxl.iam_service.application.service.custom;

import dev.cxl.iam_service.domain.enums.UserAction;

public interface ActivityService {
    void createHistoryActivity(String userId, UserAction action);
}
