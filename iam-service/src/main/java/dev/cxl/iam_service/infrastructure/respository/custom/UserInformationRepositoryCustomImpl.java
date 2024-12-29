package dev.cxl.iam_service.infrastructure.respository.custom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import dev.cxl.iam_service.application.dto.request.UserInformationSearchRequest;
import dev.cxl.iam_service.infrastructure.entity.UserInformationEntity;

public class UserInformationRepositoryCustomImpl implements UserInformationRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserInformationEntity> search(UserInformationSearchRequest request) {
        Map<String, Object> values = new HashMap<>();
        String sql = "select u from UserInformationEntity u " + createWhereQuery(request, values)
                + createOrderQuery(request.getSortBy());
        Query query = entityManager.createQuery(sql, UserInformationEntity.class);
        values.forEach(query::setParameter);
        query.setFirstResult((request.getPageIndex() - 1) * request.getPageSize());
        query.setMaxResults(request.getPageSize());
        return query.getResultList();
    }

    @Override
    public Long count(UserInformationSearchRequest request) {
        Map<String, Object> values = new HashMap<>();
        String sql = "select count(u) from UserInformationEntity u " + createWhereQuery(request, values);
        Query query = entityManager.createQuery(sql, Long.class);
        values.forEach(query::setParameter);
        return (Long) query.getSingleResult();
    }

    private String createWhereQuery(UserInformationSearchRequest request, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder();
        //        sql.append(" left join RoleEntity r on (e.roleId = r.id) ");
        sql.append(" where u.deleted = false");
        if (StringUtils.isNotBlank(request.getKeyword())) {
            sql.append(" and ( lower(u.streetName) like :keyword"
                    + " or lower(u.ward) like :keyword"
                    + " or lower(u.district) like :keyword"
                    + " or lower(u.province) like :keyword) ");
            values.put("keyword", "%" + request.getKeyword().toLowerCase() + "%");
        }
        if (StringUtils.isNotBlank(request.getYearsOfExperience().toString())) {
            sql.append(" and u.yearsOfExperience = :yearsOfExperience ");
            values.put("yearsOfExperience", request);
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
