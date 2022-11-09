package com.example.demo.Repository;

import com.example.demo.domain.Lecture;
import com.example.demo.domain.Student;
import com.example.demo.domain.TimeTable;
import com.example.demo.domain.TimeTableLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.util.Optional;

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
    public Optional<TimeTableLecture> findByTimetableAndLecture(TimeTable timeTable, Lecture lecture) {
        try {
            return Optional.ofNullable(em.createQuery("select tl from TimeTableLecture tl where tl.timeTable =:timeTable and tl.lecture =:lecture", TimeTableLecture.class)
                    .setParameter("timeTable", timeTable)
                    .setParameter("lecture", lecture)
                    .getSingleResult());
        } catch (NonUniqueResultException | NoResultException e) {
            return Optional.empty();
        }
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
