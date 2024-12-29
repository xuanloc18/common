package dev.cxl.iam_service.application.service.impl;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import dev.cxl.iam_service.domain.enums.UserAction;
import dev.cxl.iam_service.infrastructure.entity.HistoryActivityEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaHistoryActivityRepository;

@Service
public class ActivityServiceImpl {

    private final JpaHistoryActivityRepository historyActivityRepository;

    private final HttpServletRequest request;

    public ActivityServiceImpl(JpaHistoryActivityRepository historyActivityRepository, HttpServletRequest request) {
        this.historyActivityRepository = historyActivityRepository;
        this.request = request;
    }

    public void createHistoryActivity(String userId, UserAction action) {

        historyActivityRepository.save(HistoryActivityEntity.builder()
                .activityType(action.name())
                .activityName(action.getDescription())
                .userID(userId)
                .activityStart(LocalDateTime.now())
                .browserID(request.getRemoteAddr())
                .build());
    }
}
