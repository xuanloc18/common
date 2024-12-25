package dev.cxl.iam_service.infrastructure.respository.custom;

import java.util.List;

import dev.cxl.iam_service.application.dto.request.UserSearchRequest;
import dev.cxl.iam_service.infrastructure.entity.User;

public interface UserRepositoryCustom {
    List<User> search(UserSearchRequest request);

    Long count(UserSearchRequest request);
}
