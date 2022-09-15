package com.example.demo.Service;

import com.example.demo.Repository.StudentRepository;
import com.example.demo.domain.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
//@Transactional
public class RecommendLectureServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired RecommendLectureService recommendLectureService;
    @Autowired StudentRepository studentRepository;

    @Test
    public void recommendTest() throws Exception {
        //given
        Student student = studentRepository.findById(3L);
        Set<String> req_lec = new HashSet<>();

        //when
        recommendLectureService.checkAndSaveCredit(student.getId());
        System.out.println(student.getCredit().getTotalCredit());
        System.out.println("_--------------------------------");

        //then
//        req_lec = recommendLectureService.computeRequiredLecture(student); // 교필, 전필, 전선만 다룸
//        recommendLectureService.recommendLectures(student, req_lec);

    }
}