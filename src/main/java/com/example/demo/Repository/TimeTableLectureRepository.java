package com.example.demo.Repository;

import com.example.demo.domain.TimeTable;
import com.example.demo.domain.TimeTableLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class TimeTableLectureRepository {

    private final EntityManager em;

    /**
     * 시간표강의 조회
     */
    public TimeTableLecture findOne(Long id) {
        return em.find(TimeTableLecture.class, id);
    }

}
