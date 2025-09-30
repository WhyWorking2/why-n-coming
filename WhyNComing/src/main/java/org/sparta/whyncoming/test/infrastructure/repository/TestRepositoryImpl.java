package org.sparta.whyncoming.test.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.sparta.whyncoming.test.domain.entity.Test;

import java.util.List;

public class TestRepositoryImpl implements TestRepositoryCustom{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Test> searchByName(String keyword) {
        String jpql = """
            select t
            from Test t
            where (:kw is null or :kw = '' or lower(t.name) like lower(concat('%', :kw, '%')))
            order by t.id desc
            """;
        return em.createQuery(jpql, Test.class)
                .setParameter("kw", keyword == null ? "" : keyword)
                .setMaxResults(200)
                .getResultList();
    }
}
