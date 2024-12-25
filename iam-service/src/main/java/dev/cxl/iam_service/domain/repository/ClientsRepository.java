package dev.cxl.iam_service.domain.repository;

import dev.cxl.iam_service.infrastructure.entity.Clients;

import java.util.Optional;

public interface ClientsRepository  {
    Optional<Clients> findById(String id);

}
