package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.mapper.ClientsMapper;
import dev.cxl.iam_service.domain.domainentity.Clients;
import dev.cxl.iam_service.domain.repository.ClientRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.ClientsEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaClientsRepository;

@Component
public class ClientsRepositoryImpl implements ClientRepositoryDomain {
    private final JpaClientsRepository repository;
    private final ClientsMapper clientsMapper;

    public ClientsRepositoryImpl(JpaClientsRepository repository, ClientsMapper clientsMapper) {
        this.repository = repository;
        this.clientsMapper = clientsMapper;
    }

    @Override
    public Optional<Clients> findById(String id) {
        ClientsEntity entity = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CLIENT_NOT_EXITS));
        return Optional.of(new Clients(entity.getClientId(), entity.getClientSecret()));
    }

    @Override
    public List<Clients> findAllByIds(List<String> strings) {
        return List.of();
    }

    @Override
    public Clients save(Clients domain) {
        return null;
    }

    @Override
    public boolean saveAll(List<Clients> domains) {
        return false;
    }

    @Override
    public boolean delete(Clients domain) {
        return false;
    }

    @Override
    public boolean deleteById(String s) {
        return false;
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }
}
