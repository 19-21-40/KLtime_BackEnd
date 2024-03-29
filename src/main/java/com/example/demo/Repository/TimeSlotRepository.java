package com.example.demo.Repository;

import com.example.demo.domain.Lecture;
import com.example.demo.domain.Student;
import com.example.demo.domain.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TimeSlotRepository {

    private final EntityManager em;

    public void save(TimeSlot timeSlot) { em.persist(timeSlot); }

    public TimeSlot findOne(Long id) { return em.find(TimeSlot.class, id); }

    /**
     * 요일로 조회
     * @param day 요일
     * @return 같은 요일의 시간 리스트 반환
     */
    public List<TimeSlot> findByDay(String day){
        return em.createQuery("select Ts from TimeSlot Ts"
                +" where Ts.dayName=:day"
                ,TimeSlot.class)
                .setParameter("day",day)
                .getResultList();
    }

    /**
     * 요일과 시간으로 조회
     * @param dayName 요일
     * @param startTime 시작시간
     * @param endTime 종료시간
     * @return
     */
    public Optional<TimeSlot> findByTimeSlot(String dayName, String startTime, String endTime){
        try {
            return Optional.ofNullable(em.createQuery("select Ts from TimeSlot Ts"
                            + " where Ts.startTime=:startTime and Ts.endTime=:endTime and Ts.dayName=:dayName", TimeSlot.class)
                    .setParameter("startTime", startTime)
                    .setParameter("endTime", endTime)
                    .setParameter("dayName", dayName)
                    .getSingleResult());
        } catch (NoResultException e){
            return Optional.empty();
        }
    }

    /**
     * 타임 슬롯 삭제
     */
    public void delete(TimeSlot timeSlot) {
        em.remove(timeSlot);
    }



}
