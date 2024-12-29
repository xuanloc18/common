package dev.cxl.iam_service.infrastructure.respository.impl;

import org.springframework.stereotype.Component;

import dev.cxl.iam_service.domain.repository.HistoryActivityRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.HistoryActivityEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaHistoryActivityRepository;

@Component
public class HistoryActivityRepositoryImpl implements HistoryActivityRepositoryDomain {
    private final JpaHistoryActivityRepository repository;

    public HistoryActivityRepositoryImpl(JpaHistoryActivityRepository repository) {
        this.repository = repository;
    }

    public HistoryActivityEntity save(HistoryActivityEntity activity) {
        return repository.save(activity);
    }
}
