package dev.cxl.iam_service.infrastructure.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.cxl.iam_service.domain.entity.HistoryActivity;

public interface HistoryActivityRepository extends JpaRepository<HistoryActivity, String> {}
