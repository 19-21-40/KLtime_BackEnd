package com.example.demo.Repository;

import com.example.demo.domain.Department;
import com.example.demo.domain.Lecture;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class DepartmentRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Department department) {
        em.persist(department);
        return department.getId();
    }

    public Department find(Long id) {
        return em.find(Department.class, id);
    }

}
