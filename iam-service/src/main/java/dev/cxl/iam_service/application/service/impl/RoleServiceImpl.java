package dev.cxl.iam_service.application.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import dev.cxl.iam_service.infrastructure.entity.UserRoleEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.dto.request.RoleRequest;
import dev.cxl.iam_service.application.dto.response.PageResponse;
import dev.cxl.iam_service.application.dto.response.RoleResponse;
import dev.cxl.iam_service.application.mapper.RoleMapper;
import dev.cxl.iam_service.application.mapper.RolePermissionMapper;
import dev.cxl.iam_service.application.service.custom.RoleService;
import dev.cxl.iam_service.domain.domainentity.Role;
import dev.cxl.iam_service.domain.repository.RolePermissionRepositoryDomain;
import dev.cxl.iam_service.domain.repository.RoleRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.RoleEntity;
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
    private final RolePermissionRepositoryDomain rolePermissionRepository;

    public RoleServiceImpl(
            RoleRepositoryDomain roleRepository,
            RolePermissionMapper rolePermissionMapper,
            RoleMapper roleMapper,
            RolePermissionRepositoryDomain rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.rolePermissionMapper = rolePermissionMapper;
        this.roleMapper = roleMapper;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public RoleResponse create(RoleRequest request) {
        Boolean check = roleRepository.existsByCode(request.getCode());
        if (check) throw new AppException(ErrorCode.ROLE_EXISTED);
        Role roleDomain = new Role(request);
        RoleEntity role = roleMapper.toRole(roleDomain);
        if (request.getPermissions() != null && !request.getPermissions().isEmpty()) {
            rolePermissionRepository.saveAll(
                    rolePermissionMapper.toRolePermissions(roleDomain.getRolePermissionDomains()));
        }

        return roleMapper.toRoleResponse(roleRepository.save(role));
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
        RoleEntity role = roleRepository.findByCode(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        Role roleDomain = roleMapper.toRoleDomain(role);
        roleDomain.delete();
        roleRepository.save(roleMapper.toRole(roleDomain));
    }

    @Override
    public List<String> listRolesExit(List<String> roleCodes) {
        return roleRepository.findAllByCodeIn(roleCodes).stream().map(RoleEntity::getId).collect(Collectors.toList());




}
}
