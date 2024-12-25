package dev.cxl.iam_service.infrastructure.respository.impl;

import dev.cxl.iam_service.domain.repository.ClientsRepository;
import dev.cxl.iam_service.infrastructure.entity.Clients;
import dev.cxl.iam_service.infrastructure.persistent.JpaClientsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClientsRepositoryImpl implements ClientsRepository {
    private JpaClientsRepository repository;
    @Override
    public Optional<Clients> findById(String id) {
        return repository.findById(id);
    }
}
