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


    /**
     * 강의 추천 1
     * @param lecture
     * @return
     */
    public Optional<List<Lecture>> recommendLectureList1(Lecture lecture,boolean isPrimary,int year, String semester) {
        try {
            return Optional.ofNullable(em.createQuery(
                            "select tl2.lecture from TimeTableLecture tl1, TimeTableLecture tl2 "
//                                    + "left outer join fetch tl1.lecture l join l.times"
                                    + " where tl1.lecture=:lecture and tl1.timeTable.student=tl2.timeTable.student "
                                    + " and tl2.lecture<>:lecture"
                                    +" and tl1.timeTable.isPrimary=:isPrimary "
                                    +"and tl2.lecture.yearOfLecture=:year " //추가
                                    +"and tl2.lecture.semester=:semester " //추가
                                    + "group by tl2.lecture"
                                    + " order by count(tl2.lecture) desc", Lecture.class)
                    .setParameter("lecture", lecture)
                    .setParameter("isPrimary", isPrimary)
                    .setParameter("year", year) //추가
                    .setParameter("semester",semester) //추가
//                    .setMaxResults(3)
                    .getResultList());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * 강의 추천 2
     * @param lecture
     * @return
     */
    public Optional<List<Lecture>> recommendLectureList2(Lecture lecture, String departmentName, int grade,boolean isPrimary,int year,String semester) {
        try {
            return Optional.ofNullable(em.createQuery(
                            "select tl2.lecture from TimeTableLecture tl1, TimeTableLecture tl2 "
//                                    + "left outer join fetch tl1.lecture l left outer join fetch l.times"
                                    + " where tl1.lecture=:lecture and tl1.timeTable.student = tl2.timeTable.student "
                                    + " and tl2.lecture<>:lecture"
                                    + " and tl1.timeTable.student.department.name =: departmentName"
                                    + " and tl1.timeTable.student.grade=:grade "
                                    + " and tl1.timeTable.isPrimary=:isPrimary "
                                    +"and tl2.lecture.yearOfLecture=:year " //추가
                                    +"and tl2.lecture.semester=:semester " //추가
                                    + " group by tl2.lecture"
                                    + " order by count(tl2.lecture) desc", Lecture.class)
                    .setParameter("lecture", lecture)
                    .setParameter("departmentName", departmentName)
                    .setParameter("grade", grade)
                    .setParameter("isPrimary", isPrimary)
                    .setParameter("year", year) //추가
                    .setParameter("semester",semester) //추가
//                    .setMaxResults(3)
                    .getResultList());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
