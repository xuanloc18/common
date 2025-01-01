package dev.cxl.iam_service.infrastructure.respository.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.cxl.iam_service.domain.repository.UserInformationRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.UserInformationEntity;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserInformationRepository;

@Component
public class UserInformationRepositoryImpl implements UserInformationRepositoryDomain {
    private final JpaUserInformationRepository jpaUserInformationRepository;

    public UserInformationRepositoryImpl(JpaUserInformationRepository jpaUserInformationRepository) {
        this.jpaUserInformationRepository = jpaUserInformationRepository;
    }

    @Override
    public boolean existsUserInformationByUsername(String userName) {
        return jpaUserInformationRepository.existsUserInformationByUsername(userName);
    }

    @Override
    public UserInformationEntity save(UserInformationEntity userInformation) {
        return jpaUserInformationRepository.save(userInformation);
    }

    @Override
    public List<UserInformationEntity> findAll() {
        return jpaUserInformationRepository.findAll();
    }

    @Override
    public void saveAll(List<UserInformationEntity> userInformation) {
        jpaUserInformationRepository.saveAll(userInformation);
    }
}
