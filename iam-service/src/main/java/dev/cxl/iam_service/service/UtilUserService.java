package dev.cxl.iam_service.service;

import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.entity.User;
import dev.cxl.iam_service.respository.UserInformationRepository;
import dev.cxl.iam_service.respository.UserRespository;

@Service
public class UtilUserService {

    private final UserRespository userRespository;

    private final UserInformationRepository userInformationRepository;

    public UtilUserService(UserRespository userRespository, UserInformationRepository userInformationRepository) {
        this.userRespository = userRespository;
        this.userInformationRepository = userInformationRepository;
    }

    public User finUserId(String id) {
        return userRespository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public User finUserKCLId(String kCLSID) {
        return userRespository.findByUserKCLID(kCLSID).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public User finUserMail(String userMail) {
        return userRespository.findByUserMail(userMail).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public boolean userExists(String userName) {
        return userInformationRepository.existsUserInformationByUsername(userName);
    }
}
