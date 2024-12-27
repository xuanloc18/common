package dev.cxl.iam_service.domain.repository;

import java.util.List;

import dev.cxl.iam_service.infrastructure.entity.UserInformation;

public interface UserInformationRepository {
    boolean existsUserInformationByUsername(String userName);

    UserInformation save(UserInformation userInformation);

    List<UserInformation> findAll();

    List<UserInformation> saveAll(List<UserInformation> userInformations);
}
