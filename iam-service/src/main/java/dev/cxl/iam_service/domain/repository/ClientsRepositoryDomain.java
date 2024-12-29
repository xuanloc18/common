package dev.cxl.iam_service.domain.repository;

import java.util.List;
import java.util.Optional;

import dev.cxl.iam_service.infrastructure.entity.ClientsEntity;

public interface ClientsRepositoryDomain {
    Optional<ClientsEntity> findById(String id);

    List<ClientsEntity> findAll();
}
