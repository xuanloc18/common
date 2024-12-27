package dev.cxl.iam_service.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import dev.cxl.iam_service.application.dto.request.UserUpdateRequest;
import dev.cxl.iam_service.application.dto.response.UserResponse;
import dev.cxl.iam_service.domain.domainentity.UserDomain;
import dev.cxl.iam_service.infrastructure.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDomain request);

    UserDomain toUserDomain(User request);

    UserResponse toUserResponse(User user);

    User updateUser(@MappingTarget User user, UserUpdateRequest request);

    List<UserResponse> toUserResponseList(List<User> users);
}
