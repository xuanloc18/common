package dev.cxl.iam_service.respository.custom;

import java.util.List;

import dev.cxl.iam_service.dto.request.UserInformationSearchRequest;
import dev.cxl.iam_service.entity.UserInformation;

public interface UserInformationRepositoryCustom {
    List<UserInformation> search(UserInformationSearchRequest request);

    Long count(UserInformationSearchRequest request);
}
