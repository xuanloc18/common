package dev.cxl.iam_service.infrastructure.respository.impl;

import dev.cxl.iam_service.domain.repository.UserInformationRepository;
import dev.cxl.iam_service.infrastructure.entity.UserInformation;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserInformationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInformationRepositoryImpl implements UserInformationRepository {
    private final JpaUserInformationRepository jpaUserInformationRepository;

    public UserInformationRepositoryImpl(JpaUserInformationRepository jpaUserInformationRepository) {
        this.jpaUserInformationRepository = jpaUserInformationRepository;
    }

    @Override
    public boolean existsUserInformationByUsername(String userName) {
        return jpaUserInformationRepository.existsUserInformationByUsername(userName);
    }

    @Override
    public UserInformation save(UserInformation userInformation) {
        return jpaUserInformationRepository.save(userInformation);
    }

    @Override
    public List<UserInformation> findAll() {
        return jpaUserInformationRepository.findAll();
    }

    @Override
    public List<UserInformation> saveAll(List<UserInformation> userInformation) {
        return  jpaUserInformationRepository.saveAll(userInformation);
    }
}
