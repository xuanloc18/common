package dev.cxl.iam_service.application.service.custom;

import dev.cxl.iam_service.domain.domainentity.User;

public interface UtilUserService {
    User finUserId(String id);

    User finUserKCLId(String kCLSID);

    User findUserMail(String userMail);

    boolean userExists(String userName);
}
