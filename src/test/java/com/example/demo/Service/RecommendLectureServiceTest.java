package com.example.demo.Service;

import com.example.demo.Repository.LectureRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.domain.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class RecommendLectureServiceTest {
    @Autowired
    RecommendLectureService recommendLectureService;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void 전공과목출력테스트() {
        //given
        Student student = studentRepository.findById(3L);

        //when
        recommendLectureService.recommendMainLectureWithNoDup(student.getId());

        //then
    }

    @Test
    public void 교양과목출력테스트() {
        //given
        Student student = studentRepository.findById(3L);

        //when
        recommendLectureService.recommendEssBalLecturesWithNoDup(student.getId());

        //then
    }

    @Test
    public void 기초과목출력테스트() {
        //given
        Student student = studentRepository.findById(3L);
        Set<String> req_lec = new HashSet<>();

        //when
        recommendLectureService.recommendBasicLectureWithNoDup(student.getId());

        //then
//        req_lec = recommendLectureService.computeRequiredLecture(student); // 교필, 전필, 전선만 다룸
//        recommendLectureService.recommendLectures(student, req_lec);
    }



    @Test
    public void 학생의학점정보저장() {
        //given
        Student student = studentRepository.findById(3L);

        //when
        recommendLectureService.checkAndSaveCredit(student.getId());

        //then
//        req_lec = recommendLectureService.computeRequiredLecture(student); // 교필, 전필, 전선만 다룸
//        recommendLectureService.recommendLectures(student, req_lec);
    }
}