package dev.cxl.iam_service.application.service;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import dev.cxl.iam_service.infrastructure.entity.HistoryActivity;
import dev.cxl.iam_service.domain.enums.UserAction;
import dev.cxl.iam_service.infrastructure.persistent.JpaHistoryActivityRepository;

@Service
public class ActivityService {

    private final JpaHistoryActivityRepository historyActivityRepository;

    private final HttpServletRequest request;

    public ActivityService(JpaHistoryActivityRepository historyActivityRepository, HttpServletRequest request) {
        this.historyActivityRepository = historyActivityRepository;
        this.request = request;
    }

    public void createHistoryActivity(String userId, UserAction action) {

        historyActivityRepository.save(HistoryActivity.builder()
                .activityType(action.name())
                .activityName(action.getDescription())
                .userID(userId)
                .activityStart(LocalDateTime.now())
                .browserID(request.getRemoteAddr())
                .build());
    }
}
