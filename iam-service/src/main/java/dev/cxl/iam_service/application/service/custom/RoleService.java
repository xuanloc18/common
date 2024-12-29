package dev.cxl.iam_service.application.service.custom;

import java.util.List;

import dev.cxl.iam_service.application.dto.request.RoleRequest;
import dev.cxl.iam_service.application.dto.response.PageResponse;
import dev.cxl.iam_service.application.dto.response.RoleResponse;

public interface RoleService {

    RoleResponse create(RoleRequest request);

    PageResponse<RoleResponse> getAll(int page, int size);

    void delete(String id);

    List<String> listRolesExit(List<String> id);
}
