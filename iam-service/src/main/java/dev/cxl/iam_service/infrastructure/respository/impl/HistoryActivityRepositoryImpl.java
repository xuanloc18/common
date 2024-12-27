package dev.cxl.iam_service.infrastructure.respository.impl;

import org.springframework.stereotype.Component;

import dev.cxl.iam_service.domain.repository.HistoryActivityRepository;
import dev.cxl.iam_service.infrastructure.entity.HistoryActivity;
import dev.cxl.iam_service.infrastructure.persistent.JpaHistoryActivityRepository;

@Component
public class HistoryActivityRepositoryImpl implements HistoryActivityRepository {
    private final JpaHistoryActivityRepository repository;

    public HistoryActivityRepositoryImpl(JpaHistoryActivityRepository repository) {
        this.repository = repository;
    }

    public HistoryActivity save(HistoryActivity activity) {
        return repository.save(activity);
    }
}
