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
import dev.cxl.iam_service.infrastructure.entity.UserEntity;

@Service
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<UserEntity> search(UserSearchRequest request) {
        Map<String, Object> values = new HashMap<>();
        String sql = "select u from UserEntity u " + createWhereQuery(request, values)
                + createOrderQuery(request.getSortBy());
        Query query = entityManager.createQuery(sql, UserEntity.class);
        values.forEach(query::setParameter);
        // Tính toán phân trang
        int firstResult = (request.getPageIndex() - 1) * request.getPageSize();
        query.setFirstResult(firstResult);
        query.setMaxResults(request.getPageSize());

        // Lấy kết quả từ database
        List<UserEntity> resultList = query.getResultList();

        // Tạo đối tượng Pageable với pageIndex và pageSize
        Pageable pageable = PageRequest.of(request.getPageIndex() - 1, request.getPageSize());

        // Trả về PageImpl với số lượng bản ghi và các tham số phân trang
        //        new PageImpl<>(resultList, pageable, count(request));
        return new PageImpl<>(resultList, pageable, count(request));
    }

    @Override
    public Long count(UserSearchRequest request) {
        Map<String, Object> values = new HashMap<>();
        String sql = "select count(u) from UserEntity u " + createWhereQuery(request, values);
        Query query = entityManager.createQuery(sql, Long.class);
        values.forEach(query::setParameter);
        return (Long) query.getSingleResult();
    }

    private String createWhereQuery(UserSearchRequest request, Map<String, Object> values) {
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
