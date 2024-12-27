package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import dev.cxl.iam_service.domain.repository.ClientsRepository;
import dev.cxl.iam_service.infrastructure.entity.Clients;
import dev.cxl.iam_service.infrastructure.persistent.JpaClientsRepository;

@Component
public class ClientsRepositoryImpl implements ClientsRepository {
    private final JpaClientsRepository repository;

    public ClientsRepositoryImpl(JpaClientsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Clients> findById(String id) {
        return repository.findById(id);
    }
}
