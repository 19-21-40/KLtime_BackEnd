package com.example.demo.Repository;

import com.example.demo.domain.LectureTimeSlot;
import com.example.demo.domain.TimeTableLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class LectureTimeSlotRepository {

    private final EntityManager em;

    /**
     * 시간표강의 삭제
     */
    public void delete(LectureTimeSlot lectureTimeSlot) {
        em.remove(lectureTimeSlot);
    }
}
