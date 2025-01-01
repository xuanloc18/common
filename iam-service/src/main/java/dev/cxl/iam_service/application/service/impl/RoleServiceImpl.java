package dev.cxl.iam_service.application.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.dto.request.RolePermissionRequest;
import dev.cxl.iam_service.application.dto.request.RoleRequest;
import dev.cxl.iam_service.application.dto.response.PageResponse;
import dev.cxl.iam_service.application.dto.response.RoleResponse;
import dev.cxl.iam_service.application.mapper.RoleMapper;
import dev.cxl.iam_service.application.mapper.RolePermissionMapper;
import dev.cxl.iam_service.application.service.custom.PermissionService;
import dev.cxl.iam_service.application.service.custom.RoleService;
import dev.cxl.iam_service.domain.command.CreateRoleCommand;
import dev.cxl.iam_service.domain.command.RolePermissionCommand;
import dev.cxl.iam_service.domain.domainentity.Role;
import dev.cxl.iam_service.domain.repository.RoleRepositoryDomain;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepositoryDomain roleRepository;
    private final RolePermissionMapper rolePermissionMapper;
    private final RoleMapper roleMapper;
    private final PermissionService permissionService;

    public RoleServiceImpl(
            RoleRepositoryDomain roleRepository,
            RolePermissionMapper rolePermissionMapper,
            RoleMapper roleMapper,
            PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.rolePermissionMapper = rolePermissionMapper;
        this.roleMapper = roleMapper;

        this.permissionService = permissionService;
    }

    @Override
    public RoleResponse create(RoleRequest request) {
        CreateRoleCommand createRoleCommand = roleMapper.toCreateRoleCommand(request);
        Boolean check = roleRepository.existsByCode(request.getCode());
        if (check) throw new AppException(ErrorCode.ROLE_EXISTED);
        List<String> idPerExist = null;
        if (request.getPermissions() != null && !request.getPermissions().isEmpty()) {
            idPerExist = permissionService.listPerExit(request.getPermissions());
        }
        Role roleDomain = new Role(createRoleCommand, idPerExist);
        return roleMapper.toRoleResponse(roleRepository.save(roleDomain));
    }

    @Override
    public void roleAddPermission(RolePermissionRequest request) {
        RolePermissionCommand rolePermissionCommand =
                rolePermissionMapper.rolePermissionToRolePermissionCommand(request);
        Role role = roleRepository
                .findByCode(rolePermissionCommand.getRoleCode())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        List<String> listPerExits = permissionService.listPerExit(rolePermissionCommand.getPermissionIds());
        role.roleAddPermissions(listPerExits);
        roleRepository.save(role);
    }

    @Override
    public void roleRemovePermission(RolePermissionRequest request) {
        RolePermissionCommand rolePermissionCommand =
                rolePermissionMapper.rolePermissionToRolePermissionCommand(request);
        Role role = roleRepository
                .findByCode(rolePermissionCommand.getRoleCode())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        List<String> listPerExits = permissionService.listPerExit(rolePermissionCommand.getPermissionIds());
        role.roleDeletePermissions(listPerExits);
        roleRepository.save(role);
    }

    @Override
    public PageResponse<RoleResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var roles = roleRepository.findAll(pageable);
        return PageResponse.<RoleResponse>builder()
                .totalPages(roles.getTotalPages())
                .currentPage(page)
                .totalElements(roles.getTotalElements())
                .pageSize(size)
                .data(roles.getContent().stream()
                        .map(roleMapper::toRoleResponse)
                        .toList())
                .build();
    }

    @Override
    public void delete(String id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        role.delete();
        roleRepository.save(role);
    }

    @Override
    public List<String> listRolesExit(List<String> roleCodes) {
        return roleRepository.findAllByCodeIn(roleCodes).stream()
                .map(Role::getId)
                .collect(Collectors.toList());
    }
}
