package dev.cxl.iam_service.application.service;

import dev.cxl.iam_service.domain.domainentity.RoleDomain;
import dev.cxl.iam_service.domain.repository.RoleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.dto.request.RoleRequest;
import dev.cxl.iam_service.application.dto.response.PageResponse;
import dev.cxl.iam_service.application.dto.response.RoleResponse;
import dev.cxl.iam_service.infrastructure.entity.Role;
import dev.cxl.iam_service.application.mapper.RoleMapper;
import dev.cxl.iam_service.infrastructure.persistent.JpaRoleRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }


    public RoleResponse create(RoleRequest request) {
        Boolean check = roleRepository.existsByCode(request.getCode());
        if (check) throw new AppException(ErrorCode.ROLE_EXISTED);
        RoleDomain roleDomain=new RoleDomain().create(request);
        Role role = roleMapper.toRole(roleDomain);
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
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
                        .map(role -> roleMapper.toRoleResponse(role))
                        .toList())
                .build();
    }

    public void delete(String id) {
        Role role = roleRepository.findByCode(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        RoleDomain roleDomain=roleMapper.toRoleDomain(role);
        roleDomain.delete();
        roleRepository.save(roleMapper.toRole(roleDomain));
    }
}
