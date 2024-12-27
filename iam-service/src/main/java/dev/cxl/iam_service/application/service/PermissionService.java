package dev.cxl.iam_service.application.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.dto.request.PermissionRequest;
import dev.cxl.iam_service.application.dto.response.PageResponse;
import dev.cxl.iam_service.application.dto.response.PermissionResponse;
import dev.cxl.iam_service.application.mapper.PermissionMapper;
import dev.cxl.iam_service.domain.domainentity.PermissionDomain;
import dev.cxl.iam_service.domain.repository.PermissionRepository;
import dev.cxl.iam_service.infrastructure.entity.Permission;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRespository;

    private final PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest request) {
        Boolean check =
                permissionRespository.existsByResourceCodeAndScope(request.getResourceCode(), request.getScope());
        if (check) throw new AppException(ErrorCode.PERMISSION_EXISTED);
        PermissionDomain permissionDomain = new PermissionDomain(request);
        Permission permission = permissionMapper.toPermission(permissionDomain);
        return permissionMapper.toPermissionResponse(permissionRespository.save(permission));
    }

    public PageResponse<PermissionResponse> getListsPer(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var data = permissionRespository.findAll(pageable);
        return PageResponse.<PermissionResponse>builder()
                .pageSize(size)
                .currentPage(page)
                .totalElements(data.getTotalElements())
                .totalPages(data.getTotalPages())
                .data(data.getContent().stream()
                        .map(permissionMapper::toPermissionResponse)
                        .toList())
                .build();
    }

    public void delete(String id) {
        Permission permission = permissionRespository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        PermissionDomain permissionDomain = permissionMapper.toPermissionDomain(permission);
        permissionDomain.delete();
        permissionRespository.save(permissionMapper.toPermission(permissionDomain));
    }
}
