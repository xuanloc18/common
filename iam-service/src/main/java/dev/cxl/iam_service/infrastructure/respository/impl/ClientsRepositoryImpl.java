package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import dev.cxl.iam_service.infrastructure.entity.ClientsEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaClientsRepository;

@Component
public class ClientsRepositoryImpl  {
    private final JpaClientsRepository repository;

    public ClientsRepositoryImpl(JpaClientsRepository repository) {
        this.repository = repository;
    }

    public Optional<ClientsEntity> findById(String id) {
        return repository.findById(id);
    }

    public List<ClientsEntity> findAll() {
        return repository.findAll();
    }


}
