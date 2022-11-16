package com.example.demo.Service;

import com.example.demo.Repository.LectureRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.domain.Lecture;
import com.example.demo.domain.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class RecommendLectureServiceTest {
    @Autowired
    GraduationRequirementService graduationRequirementService;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void 전공과목출력테스트() {
        //given
        Student student = studentRepository.findById(3L);

        //when
        graduationRequirementService.recommendMainLectureWithNoDup(student.getNumber());

        //then
    }

    @Test
    public void 교양과목출력테스트() {

        //when
        graduationRequirementService.recommendEssBalLecturesWithNoDup("2021203078");

        //then
    }

    @Test
    public void 균형교양과목만_출력테스트() {
        //given
        Student student = studentRepository.findById(3L);

        //when
        Map<String, List<Lecture>> result = graduationRequirementService.recommendOnlyBalLecturesWithNoDup(student.getNumber());

        //then
        System.out.println(result);
    }

    @Test
    public void 필수교양과목만_출력테스트() {
        //given
        Student student = studentRepository.findById(3L);

        //when
        Map<String, List<Lecture>> result = graduationRequirementService.recommendOnlyEssLecturesWithNoDup(student.getNumber());

        //then
        System.out.println(result);
    }

    @Test
    public void 기초과목출력테스트() {
        //given
        Student student = studentRepository.findById(3L);
        Set<String> req_lec = new HashSet<>();

        //when
        graduationRequirementService.recommendBasicLectureWithNoDup(student.getNumber());

        //then
//        req_lec = recommendLectureService.computeRequiredLecture(student); // 교필, 전필, 전선만 다룸
//        recommendLectureService.recommendLectures(student, req_lec);
    }



    @Test
    public void 학생의학점정보저장() {
        //given

        //when
        graduationRequirementService.checkAndSaveCredit("2021203078");

        //then
//        req_lec = recommendLectureService.computeRequiredLecture(student); // 교필, 전필, 전선만 다룸
//        recommendLectureService.recommendLectures(student, req_lec);
    }
}