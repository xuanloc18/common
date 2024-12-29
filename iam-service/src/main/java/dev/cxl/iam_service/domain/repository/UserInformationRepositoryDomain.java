package dev.cxl.iam_service.domain.repository;

import java.util.List;

import dev.cxl.iam_service.infrastructure.entity.UserInformationEntity;

public interface UserInformationRepositoryDomain {
    boolean existsUserInformationByUsername(String userName);

    UserInformationEntity save(UserInformationEntity userInformation);

    List<UserInformationEntity> findAll();

    List<UserInformationEntity> saveAll(List<UserInformationEntity> userInformation);
}
