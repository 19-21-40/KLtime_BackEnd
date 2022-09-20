package com.example.demo.Service;

import com.example.demo.Repository.DepartmentRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.domain.Department;
import com.example.demo.domain.Student;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class StudentServiceTest {

    @Autowired StudentService studentService;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    @Test
//    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Department department = departmentRepository.findById(1L);
        Student student = new Student("김철수", department, 2, 2021);
        //when
        Long saveId = studentService.join(student);
        //then
        assertEquals(student, studentRepository.findById(saveId));
    }

    //학번 생기면 그때 해보기
    @Test
//    @Rollback(false)
    public void 증복_회원_예외() throws Exception {
        //given
        Department 소프트웨어학부 = departmentRepository.findById(1L);
        Department 컴퓨터정보공학부 = departmentRepository.findById(2L);
        Student student1 = studentRepository.findById(5L);
        Student student2 = new Student("김수연", 소프트웨어학부, 2, 2021);
        //when
        IllegalStateException e = assertThrows(IllegalStateException.class,()-> studentService.join(student2));
        //then
        assertEquals("이미 존재하는 학번(ID)입니다.", e.getMessage());
    }

}