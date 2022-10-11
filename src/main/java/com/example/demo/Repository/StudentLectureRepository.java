package com.example.demo.Repository;
import com.example.demo.domain.Student;
import com.example.demo.domain.StudentLecture;
import com.example.demo.domain.TimeTable;
import com.example.demo.domain.TimeTableLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudentLectureRepository {
    private final EntityManager em;

    public StudentLecture findById(Long id) {
        return em.find(StudentLecture.class, id);
    }

    public List<StudentLecture> findByStudentAndYearAndSemester(Student student, int yearOfLecture, int takesSemester){
        return em.createQuery("select sl from StudentLecture sl" +
                        " where sl.student =:student" +
                        " and sl.lecture.yearOfLecture =: yearOfLecture" +
                        " and sl.takesSemester =:takesSemester", StudentLecture.class)
                .setParameter("student", student)
                .setParameter("yearOfLecture", yearOfLecture)
                .setParameter("takesSemester", takesSemester)
                .getResultList();
    }

    /**
     * studentlecture 삭제
     */
    public void delete(StudentLecture studentLecture) {
        em.remove(studentLecture);
    }
}
