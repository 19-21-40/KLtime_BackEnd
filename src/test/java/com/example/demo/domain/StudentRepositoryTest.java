package com.example.demo.domain;

import com.example.demo.Repository.StudentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void testStudent() throws Exception {
        //given
        Student student = new Student();
        student.setName("memberA");

        //when
        Long savedId = studentRepository.save(student);
        Student findStudent = studentRepository.find(savedId);

        //then
        Assertions.assertEquals(findStudent.getName(), student.getName());
    }

}