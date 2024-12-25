package dev.cxl.iam_service.infrastructure.persistent;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.cxl.iam_service.infrastructure.entity.HistoryActivity;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaHistoryActivityRepository extends JpaRepository<HistoryActivity, String> {

}
