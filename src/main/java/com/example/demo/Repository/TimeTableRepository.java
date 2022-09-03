package com.example.demo.Repository;

import com.example.demo.domain.TimeTable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class TimeTableRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(TimeTable timeTable) {
        em.persist(timeTable);
        return timeTable.getId();
    }

    public TimeTable find(Long id) {
        return em.find(TimeTable.class, id);
    }

}
