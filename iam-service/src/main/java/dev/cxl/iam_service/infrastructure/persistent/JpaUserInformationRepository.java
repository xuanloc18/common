package dev.cxl.iam_service.infrastructure.persistent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.cxl.iam_service.infrastructure.entity.UserInformation;

@Repository
public interface JpaUserInformationRepository extends JpaRepository<UserInformation, String> {
    boolean existsUserInformationByUsername(String userName);
}
