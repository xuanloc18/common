package dev.cxl.iam_service.application.mapper;

import org.mapstruct.Mapper;

import dev.cxl.iam_service.domain.domainentity.Clients;
import dev.cxl.iam_service.infrastructure.entity.ClientsEntity;

@Mapper(componentModel = "spring")
public interface ClientsMapper {
    Clients toClients(ClientsEntity clientsEntity);
}
