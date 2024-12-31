package dev.cxl.iam_service.application.service.impl;

import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.service.custom.UtilUserService;
import dev.cxl.iam_service.domain.domainentity.User;
import dev.cxl.iam_service.domain.repository.UserInformationRepositoryDomain;
import dev.cxl.iam_service.domain.repository.UserRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;

@Service
public class UtilUserServiceImpl implements UtilUserService {

    private final UserRepositoryDomain userRepository;

    private final UserInformationRepositoryDomain userInformationRepository;

    public UtilUserServiceImpl(
            UserRepositoryDomain userRepository, UserInformationRepositoryDomain userInformationRepository) {
        this.userRepository = userRepository;
        this.userInformationRepository = userInformationRepository;
    }

    @Override
    public UserEntity finUserId(String id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public UserEntity finUserKCLId(String kCLSID) {
        return userRepository.findByUserKCLID(kCLSID).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public UserEntity finUserMail(String userMail) {
        return userRepository.findByUserMail(userMail).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public boolean userExists(String userName) {
        return userInformationRepository.existsUserInformationByUsername(userName);
    }

    @Override
    public User getUserDomainById(String userID) {
        return userRepository.getUserByUserId(userID);
    }

    @Override
    public User getUserDomainByMail(String userMail) {
        return userRepository.getUserByEmail(userMail);
    }
}
