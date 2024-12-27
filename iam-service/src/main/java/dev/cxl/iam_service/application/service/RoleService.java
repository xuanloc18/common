package dev.cxl.iam_service.application.service;

import java.util.List;
import java.util.stream.Collectors;

import dev.cxl.iam_service.application.mapper.RolePermissionMapper;
import dev.cxl.iam_service.domain.domainentity.PermissionDomain;
import dev.cxl.iam_service.domain.repository.RolePermissionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.dto.request.RoleRequest;
import dev.cxl.iam_service.application.dto.response.PageResponse;
import dev.cxl.iam_service.application.dto.response.RoleResponse;
import dev.cxl.iam_service.application.mapper.RoleMapper;
import dev.cxl.iam_service.domain.domainentity.RoleDomain;
import dev.cxl.iam_service.domain.repository.RoleRepository;
import dev.cxl.iam_service.infrastructure.entity.Role;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionMapper rolePermissionMapper;


    private final RoleMapper roleMapper;
    private final RolePermissionRepository rolePermissionRepository;

    public RoleService(
            RoleRepository roleRepository, RolePermissionMapper rolePermissionMapper, RoleMapper roleMapper, RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.rolePermissionMapper = rolePermissionMapper;
        this.roleMapper = roleMapper;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public RoleResponse create(RoleRequest request) {
        Boolean check = roleRepository.existsByCode(request.getCode());
        if (check) throw new AppException(ErrorCode.ROLE_EXISTED);
        RoleDomain roleDomain = new RoleDomain(request);
        Role role = roleMapper.toRole(roleDomain);
        if (request.getPermissions() != null && !request.getPermissions().isEmpty()) {
            rolePermissionRepository.saveAll(rolePermissionMapper.toRolePermissions(roleDomain.getRolePermissionDomains()));
        }

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

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

    public void delete(String id) {
        Role role = roleRepository.findByCode(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        RoleDomain roleDomain = roleMapper.toRoleDomain(role);
        roleDomain.delete();
        roleRepository.save(roleMapper.toRole(roleDomain));
    }

    public List<String> listRolesExit(List<String> id) {
        return roleRepository.findAll(id).stream().map(Role::getId).collect(Collectors.toList());
    }
}
