package dev.cxl.iam_service.domain.repository;

import java.util.Optional;

import dev.cxl.iam_service.infrastructure.entity.Clients;

public interface ClientsRepository {
    Optional<Clients> findById(String id);
}
