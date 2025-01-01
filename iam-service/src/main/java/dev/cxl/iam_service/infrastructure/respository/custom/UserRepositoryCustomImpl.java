package dev.cxl.iam_service.infrastructure.respository.custom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.cxl.iam_service.application.dto.request.UserSearchRequest;
import dev.cxl.iam_service.application.mapper.UserMapper;
import dev.cxl.iam_service.domain.query.UserSearchQuery;
import dev.cxl.iam_service.infrastructure.entity.UserEntity;

@Service
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final UserMapper userMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public UserRepositoryCustomImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Page<UserEntity> search(UserSearchRequest request) {
        UserSearchQuery userSearchQuery = userMapper.toUserUserSearchCommand(request);
        Map<String, Object> values = new HashMap<>();
        String sql = "select u from UserEntity u " + createWhereQuery(userSearchQuery, values)
                + createOrderQuery(userSearchQuery.getSortBy());
        Query query = entityManager.createQuery(sql, UserEntity.class);
        values.forEach(query::setParameter);
        // Tính toán phân trang
        int firstResult = (userSearchQuery.getPageIndex() - 1) * userSearchQuery.getPageSize();
        query.setFirstResult(firstResult);
        query.setMaxResults(userSearchQuery.getPageSize());

        // Lấy kết quả từ database
        List<UserEntity> resultList = query.getResultList();

        // Tạo đối tượng Pageable với pageIndex và pageSize
        Pageable pageable = PageRequest.of(userSearchQuery.getPageIndex() - 1, userSearchQuery.getPageSize());

        // Trả về PageImpl với số lượng bản ghi và các tham số phân trang
        //        new PageImpl<>(resultList, pageable, count(userSearchQuery));
        return new PageImpl<>(resultList, pageable, count(userSearchQuery));
    }

    @Override
    public Long count(UserSearchQuery request) {
        Map<String, Object> values = new HashMap<>();
        String sql = "select count(u) from UserEntity u " + createWhereQuery(request, values);
        Query query = entityManager.createQuery(sql, Long.class);
        values.forEach(query::setParameter);
        return (Long) query.getSingleResult();
    }

    private String createWhereQuery(UserSearchQuery request, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder();
        //        sql.append(" left join RoleEntity r on (e.roleId = r.id) ");
        sql.append(" where u.deleted = false");
        if (StringUtils.isNotBlank(request.getKeyword())) {
            sql.append(" and ( lower(u.userName) like :keyword"
                    + " or lower(u.userMail) like :keyword"
                    + " or lower(u.firstName) like :keyword"
                    + " or lower(u.lastName) like :keyword) ");
            values.put("keyword", "%" + request.getKeyword().toLowerCase() + "%");
        }
        if (StringUtils.isNotBlank(request.getUserName())) {
            sql.append(" and u.userName = :userName ");
            values.put("userName", request.getUserName());
        }
        if (!StringUtils.isEmpty(request.getUserMail())) {
            sql.append(" and u.userMail in :userMail ");
            values.put("userMail", request.getUserMail());
        }
        return sql.toString();
    }

    public StringBuilder createOrderQuery(String sortBy) {
        StringBuilder hql = new StringBuilder("");
        if (StringUtils.isNotBlank(sortBy)) {
            hql.append(" order by u.").append(sortBy.replace(".", " "));
        } else {
            hql.append(" order by u.dateOfBirth desc ");
        }
        return hql;
    }
}
