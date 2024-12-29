package dev.cxl.iam_service.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import dev.cxl.iam_service.application.dto.request.UserUpdateRequest;
import dev.cxl.iam_service.application.dto.response.UserResponse;
import dev.cxl.iam_service.domain.domainentity.User;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUser(User request);

    User toUserDomain(UserEntity request);

    UserResponse toUserResponse(UserEntity user);

    UserEntity updateUser(@MappingTarget UserEntity user, UserUpdateRequest request);

    List<UserResponse> toUserResponseList(List<UserEntity> users);
}
