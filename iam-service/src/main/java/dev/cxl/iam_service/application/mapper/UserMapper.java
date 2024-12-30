package dev.cxl.iam_service.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import dev.cxl.iam_service.application.dto.request.UserCreationRequest;
import dev.cxl.iam_service.application.dto.request.UserReplacePass;
import dev.cxl.iam_service.application.dto.request.UserUpdateRequest;
import dev.cxl.iam_service.application.dto.response.UserResponse;
import dev.cxl.iam_service.domain.command.UserCreationCommand;
import dev.cxl.iam_service.domain.command.UserReplacePassCommand;
import dev.cxl.iam_service.domain.command.UserUpdateCommand;
import dev.cxl.iam_service.domain.domainentity.User;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUserEntity(User request);

    UserUpdateCommand toUserUpdateCommand(UserUpdateRequest request);

    User toUserDomain(UserEntity request);

    UserResponse toUserResponse(UserEntity user);

    UserCreationCommand toUserUserCreationCommand(UserCreationRequest request);

    UserEntity updateUser(@MappingTarget UserEntity user, UserUpdateRequest request);

    List<UserResponse> toUserResponseList(List<UserEntity> users);

    UserReplacePassCommand toUserReplacePassCommand(UserReplacePass request);
}
