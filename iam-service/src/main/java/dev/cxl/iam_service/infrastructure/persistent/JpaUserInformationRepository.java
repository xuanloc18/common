package dev.cxl.iam_service.infrastructure.persistent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.cxl.iam_service.infrastructure.entity.UserInformationEntity;

@Repository
public interface JpaUserInformationRepository extends JpaRepository<UserInformationEntity, String> {
    boolean existsUserInformationByUsername(String userName);
}
