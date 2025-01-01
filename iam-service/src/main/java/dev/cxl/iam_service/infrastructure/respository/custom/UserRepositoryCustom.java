package dev.cxl.iam_service.infrastructure.respository.custom;

import org.springframework.data.domain.Page;

import dev.cxl.iam_service.application.dto.request.UserSearchRequest;
import dev.cxl.iam_service.domain.query.UserSearchQuery;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;

public interface UserRepositoryCustom {
    Page<UserEntity> search(UserSearchRequest request);

    Long count(UserSearchQuery request);
}
