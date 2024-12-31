package dev.cxl.iam_service.application.service.custom;

import java.util.List;

import com.evo.common.exception.AppException;

import dev.cxl.iam_service.application.dto.request.PermissionRequest;
import dev.cxl.iam_service.application.dto.response.PageResponse;
import dev.cxl.iam_service.application.dto.response.PermissionResponse;

public interface PermissionService {

    PermissionResponse createPermission(PermissionRequest request) throws AppException;

    PageResponse<PermissionResponse> getListsPer(int page, int size);

    void delete(String id) throws AppException;

    List<String> listPerExit(List<String> permissionId);
}
