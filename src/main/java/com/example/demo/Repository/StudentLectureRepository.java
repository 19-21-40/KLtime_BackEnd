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
    public StudentLecture findByStudentAndLecture(Lecture lecture,Student student){
        return em.createQuery("select sl from StudentLecture sl where sl.lecture=: lecture and sl.student=:student",StudentLecture.class)
                .setParameter("lecture",lecture)
                .setParameter("student",student)
                .getSingleResult();
    }

    public List<StudentLecture> findByStudentAndYearAndSemester(Student student, int yearOfLecture, String takesSemester){
        return em.createQuery("select sl from StudentLecture sl" +
                        " where sl.student =:student" +
                        " and sl.lecture.yearOfLecture =: yearOfLecture" +
                        " and sl.takesSemester =:takesSemester", StudentLecture.class)
                .setParameter("student", student)
                .setParameter("yearOfLecture", yearOfLecture)
                .setParameter("takesSemester", takesSemester)
                .getResultList();
    }

    public Optional<StudentLecture> findByStudentAndLectureName(Student student, String lectureName){
        try{
            return Optional.ofNullable(em.createQuery("select sl from StudentLecture sl join fetch sl.lecture L where sl.student=:student and L.name=:lectureName",StudentLecture.class)
                    .setParameter("student",student)
                    .setParameter("lectureName",lectureName)
                    .getSingleResult());
        }catch (NoResultException e){
            return Optional.empty();
        }
    }

    /**
     * studentlecture 삭제
     */
    public void delete(StudentLecture studentLecture) {
        em.remove(studentLecture);
    }
}
