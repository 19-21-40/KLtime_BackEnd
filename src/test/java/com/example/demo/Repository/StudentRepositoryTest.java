package com.example.demo.Repository;

import antlr.debug.DebuggingParser;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.domain.Department;
import com.example.demo.domain.Lecture;
import com.example.demo.domain.Student;
import com.example.demo.domain.StudentLecture;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    EntityManager em;

    @Test
    @Rollback(false)
    public void testStudent() throws Exception {
        //given
        Student student = new Student();
        student.setName("memberA");

        //when
        Long savedId = studentRepository.save(student);
        Student findStudent = studentRepository.findById(savedId);

        //then
//        Assertions.assertEquals(findStudent.getName(), student.getName());
    }

    @Test
    @Rollback(false)
    public void 강의로_학생검색() throws Exception {
        //given
        Department dept1 = new Department("소프트웨어학부", "소융대");
        Department dept2 = new Department("컴퓨터정보공학부", "소융대");

        Student student1 = new Student("이성훈", dept1, 1);
        Student student2 = new Student("나부겸", dept1, 2);
        Student student3 = new Student("김수연", dept2, 1);
        Student student4 = new Student("신재민", dept2, 1);

        Lecture lecture1 = new Lecture("1000-1-2345-87", "공학설계입문");
        Lecture lecture2 = new Lecture("1001-2-3456-89", "디지털논리");

        //when
        departmentRepository.save(dept1);
        departmentRepository.save(dept2);
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);
        lectureRepository.save(lecture1);
        lectureRepository.save(lecture2);

        student1.addLectureToStudent(lecture1);
        student1.addLectureToStudent(lecture2);
        student2.addLectureToStudent(lecture1);
        student2.addLectureToStudent(lecture2);
        student3.addLectureToStudent(lecture2);

        em.flush();

        //then
        List<Student> resultList = studentRepository.findByLecture(lecture2);
        for ( Student student : resultList ) {
            System.out.println("grade: " + student.getGrade() + ", name: " + student.getName() + ", dept: " + student.getDepartment().getName());
        }

        List<Student> resultList2 = studentRepository.findByLectureAndDept(lecture2, dept1);
        for ( Student student : resultList2 ) {
            System.out.println("grade: " + student.getGrade() + ", name: " + student.getName() + ", dept: " + student.getDepartment().getName());
        }

        List<Student> resultList3 = studentRepository.findByLectureAndDeptAndGrade(lecture2, dept1, student1.getGrade());
        for ( Student student : resultList3 ) {
            System.out.println("grade: " + student.getGrade() + ", name: " + student.getName() + ", dept: " + student.getDepartment().getName());        }

    }

}