package com.example.demo.Repository;

import com.example.demo.domain.GradCondition;
import com.example.demo.domain.Student;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class GradConditionRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(GradCondition gradCondition) {
        em.persist(gradCondition);
        return gradCondition.getId();
    }

    public GradCondition find(Long id) {
        return em.find(GradCondition.class, id);
    }
}
