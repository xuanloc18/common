package dev.cxl.iam_service.infrastructure.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.cxl.iam_service.domain.entity.UserInformation;

@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, String> {
    boolean existsUserInformationByUsername(String userName);
}
