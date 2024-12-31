package dev.cxl.iam_service.application.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.dto.request.PermissionRequest;
import dev.cxl.iam_service.application.dto.response.PageResponse;
import dev.cxl.iam_service.application.dto.response.PermissionResponse;
import dev.cxl.iam_service.application.mapper.PermissionMapper;
import dev.cxl.iam_service.application.service.custom.PermissionService;
import dev.cxl.iam_service.domain.command.PermissionCommand;
import dev.cxl.iam_service.domain.domainentity.Permission;
import dev.cxl.iam_service.domain.repository.PermissionRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.PermissionEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepositoryDomain permissionRepository;

    private final PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest request) {
        Boolean check =
                permissionRepository.existsByResourceCodeAndScope(request.getResourceCode(), request.getScope());
        if (check) throw new AppException(ErrorCode.PERMISSION_EXISTED);

        PermissionCommand permissionCommand = permissionMapper.toPermissionCommand(request);
        Permission permission = new Permission(permissionCommand);

        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    public PageResponse<PermissionResponse> getListsPer(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var data = permissionRepository.findAll(pageable);
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
        PermissionEntity permission =
                permissionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        Permission permissionDomain = permissionMapper.toPermissionDomain(permission);
        permissionDomain.delete();
        permissionRepository.save(permissionMapper.toPermission(permission));
    }

    public List<String> listPerExit(List<String> permissionId) {
        return permissionRepository.findAllByIdIn(permissionId).stream()
                .map(PermissionEntity::getId)
                .collect(Collectors.toList());
    }
}
