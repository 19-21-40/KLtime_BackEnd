package com.example.demo.Repository;

import com.example.demo.domain.Department;
import com.example.demo.domain.Lecture;
import com.example.demo.domain.Student;
import com.example.demo.domain.StudentLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class StudentRepository {

    private final EntityManager em;

    public Long save(Student student) {
        em.persist(student);
        return student.getId();
    }

    public Student findById(Long id) {
        return em.find(Student.class, id);
    }


    public List<Student> findByLecture(Lecture lecture) {
        return em.createQuery("select s from Student s left join s.myLectures sl where sl.lecture =:lecture", Student.class)
                .setParameter("lecture", lecture)
                .getResultList();
    }
//
    public List<Student> findByLectureAndDept(Lecture lecture, Department dept) {
        return em.createQuery("select s from Student s left join s.myLectures sl where sl.lecture =:lecture and s.department =:dept", Student.class)
                .setParameter("lecture", lecture)
                .setParameter("dept", dept)
                .getResultList();

    }

    public List<Student> findByLectureAndDeptAndGrade(Lecture lecture, Department dept, int grade) {
        return em.createQuery("select s from Student s left join s.myLectures sl "
                + "where sl.lecture =:lecture and s.department =:dept and s.grade =:grade", Student.class)
                .setParameter("lecture", lecture)
                .setParameter("dept", dept)
                .setParameter("grade", grade)
                .getResultList();

    }
}
