package com.example.demo.Repository;

import com.example.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentLectureRepository {
    private final EntityManager em;

    public StudentLecture findById(Long id) {
        return em.find(StudentLecture.class, id);
    }

    public StudentLecture findByStudentAndLecture(Lecture lecture, Student student) {
        return em.createQuery("select sl from StudentLecture sl where sl.lecture=: lecture and sl.student=:student", StudentLecture.class)
                .setParameter("lecture", lecture)
                .setParameter("student", student)
                .getSingleResult();
    }

    public List<StudentLecture> findByStudentAndTimeTable(Student student, TimeTable timeTable) {
        return em.createQuery("select sl from StudentLecture sl" +
                        " where sl.student =:student" +
                        " and sl.lecture.yearOfLecture =: yearOfLecture" +
                        " and sl.takesSemester =:takesSemester", StudentLecture.class)
                .setParameter("student", student)
                .setParameter("yearOfLecture", timeTable.getYearOfTimetable())
                .setParameter("takesSemester", timeTable.getSemester())
                .getResultList();
    }

    public List<StudentLecture> findByStudentAndYearAndSemester(Student student, int yearOfLecture, String takesSemester) {
        return em.createQuery("select sl from StudentLecture sl" +
                        " where sl.student =:student" +
                        " and sl.lecture.yearOfLecture =: yearOfLecture" +
                        " and sl.takesSemester =:takesSemester", StudentLecture.class)
                .setParameter("student", student)
                .setParameter("yearOfLecture", yearOfLecture)
                .setParameter("takesSemester", takesSemester)
                .getResultList();
    }

    public Optional<StudentLecture> findByStudentAndLectureName(Student student, String lectureName) {
        try {
            return Optional.ofNullable(em.createQuery("select sl from StudentLecture sl join fetch sl.lecture L where sl.student=:student and L.name=:lectureName", StudentLecture.class)
                    .setParameter("student", student)
                    .setParameter("lectureName", lectureName)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * studentlecture 삭제
     */
    public void delete(StudentLecture studentLecture) {
        em.remove(studentLecture);
    }


    public Optional<List<String>> recommendLectureNameList(String lectureName) {
        try {
            return Optional.ofNullable(em.createQuery(
                            "select sl2.lecture.name from StudentLecture sl1 join StudentLecture sl2"
                                    + " where sl1.lecture.name=:lectureName and sl1.student=sl2.student group by sl2.lecture.name"
                                    + " order by count(sl2)", String.class)
                    .setParameter("lectureName", lectureName)
                    .setMaxResults(3)
                    .getResultList());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
