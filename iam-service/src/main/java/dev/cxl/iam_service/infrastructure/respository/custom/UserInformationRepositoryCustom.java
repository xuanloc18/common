package dev.cxl.iam_service.infrastructure.respository.custom;

import java.util.List;

import dev.cxl.iam_service.application.dto.request.UserInformationSearchRequest;
import dev.cxl.iam_service.infrastructure.entity.UserInformation;

public interface UserInformationRepositoryCustom {
    List<UserInformation> search(UserInformationSearchRequest request);

    Long count(UserInformationSearchRequest request);
}
