package com.example.demo.Repository;

import com.example.demo.domain.Lecture;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class LectureRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Lecture lecture) {
        em.persist(lecture);
        return lecture.getId();
    }

    public Lecture find(Long id) {
        return em.find(Lecture.class, id);
    }
}
