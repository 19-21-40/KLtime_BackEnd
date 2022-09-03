package com.example.demo.Repository;

import com.example.demo.domain.Student;
import com.example.demo.domain.TimeSlot;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class TimeSlotRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(TimeSlot timeSlot) {
        em.persist(timeSlot);
        return timeSlot.getId();
    }

    public TimeSlot find(Long id) {
        return em.find(TimeSlot.class, id);
    }

}
