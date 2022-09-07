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

    public Student findbyId(Long id) {
        return em.find(Student.class, id);
    }

    /** 나중에 서비스계층으로 옮겨야함 */
    public void addLectureToStudent(Student student, Lecture lecture) {
        StudentLecture st = new StudentLecture(student, lecture);

        student.getMyLectures().add(st);
    }

    public List<Student> findByLecture(Lecture lecture) {
        return em.createQuery("select S from Student S left join StudentLecture SL on S.Id=SL.id left join Lecture L on L.id=SL.id", Student.class)
                .setParameter()
                .
    }
//
//    public List<Student> findByLectureAndDept(Lecture lecture, Department dept) {
//
//    }
//
//    public List<Student> findByLectureAndDeptAndGrade(Lecture lecture, Department dept) {
//
//    }
}
