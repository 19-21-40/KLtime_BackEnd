package com.example.demo.Repository;

import com.example.demo.domain.Student;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class StudentRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Student student) {
        em.persist(student);
        return student.getId();
    }

    public Student find(Long id) {
        return em.find(Student.class, id);
    }
}
