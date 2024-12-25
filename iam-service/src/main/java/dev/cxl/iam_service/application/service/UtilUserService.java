package dev.cxl.iam_service.application.service;

import dev.cxl.iam_service.domain.repository.UserInformationRepository;
import dev.cxl.iam_service.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.infrastructure.entity.User;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserInformationRepository;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserRepository;

@Service
public class UtilUserService {

    private final UserRepository userRepository;

    private final UserInformationRepository userInformationRepository;

    public UtilUserService(UserRepository userRepository, UserInformationRepository userInformationRepository) {
        this.userRepository = userRepository;
        this.userInformationRepository = userInformationRepository;
    }

    public User finUserId(String id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public User finUserKCLId(String kCLSID) {
        return userRepository.findByUserKCLID(kCLSID).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public User finUserMail(String userMail) {
        return userRepository.findByUserMail(userMail).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public boolean userExists(String userName) {
        return userInformationRepository.existsUserInformationByUsername(userName);
    }
}
