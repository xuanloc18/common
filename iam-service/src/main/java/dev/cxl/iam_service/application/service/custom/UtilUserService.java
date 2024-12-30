package dev.cxl.iam_service.application.service.custom;

import dev.cxl.iam_service.domain.domainentity.User;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;

public interface UtilUserService {
    UserEntity finUserId(String id);

    UserEntity finUserKCLId(String kCLSID);

    UserEntity finUserMail(String userMail);

    boolean userExists(String userName);

    User getUserDomainById(String userID);

    User getUserDomainByMail(String userMail);
}
