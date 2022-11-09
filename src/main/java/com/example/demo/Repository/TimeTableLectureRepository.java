package com.example.demo.Repository;

import com.example.demo.domain.Lecture;
import com.example.demo.domain.Student;
import com.example.demo.domain.TimeTable;
import com.example.demo.domain.TimeTableLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

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

    /**
     * 시간표 별 시간표강의 조회
     */
    public TimeTableLecture findByTimetableAndLecture(TimeTable timeTable, Lecture lecture) {
        return em.createQuery("select tl from TimeTableLecture tl where tl.timeTable =:timeTable and tl.lecture =:lecture", TimeTableLecture.class)
                .setParameter("timeTable", timeTable)
                .setParameter("lecture", lecture)
                .getSingleResult();
    }

    /**
     * 시간표 내 시간표강의 삭제
     */


    /**
     * 시간표강의 삭제
     */
    public void delete(TimeTableLecture timeTableLecture) {
        em.remove(timeTableLecture);
    }
}
