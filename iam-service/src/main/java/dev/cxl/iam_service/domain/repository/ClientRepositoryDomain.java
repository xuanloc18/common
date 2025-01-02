package dev.cxl.iam_service.domain.repository;

import java.util.Optional;

import com.evo.common.DomainRepository;

import dev.cxl.iam_service.domain.domainentity.Clients;

public interface ClientRepositoryDomain extends DomainRepository<Clients, String> {
    Optional<Clients> findById(String id);
}
