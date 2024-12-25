package dev.cxl.iam_service.domain.repository;

import dev.cxl.iam_service.infrastructure.entity.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserInformationRepository {
    boolean existsUserInformationByUsername(String userName);
    UserInformation save(UserInformation userInformation);
    List<UserInformation> findAll();
    List<UserInformation> saveAll(List<UserInformation> userInformations);
}
