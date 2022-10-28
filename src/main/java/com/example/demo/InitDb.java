package com.example.demo;


import com.example.demo.Repository.DepartmentRepository;
import com.example.demo.Repository.GradConditionRepository;
import com.example.demo.Repository.LectureRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class InitDb {


    private final InitService initService;

    @PostConstruct
    public void init(){

//        initService.dbInit1();


    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final DepartmentRepository departmentRepository;
        private final GradConditionRepository gradConditionRepository;
        private final StudentRepository studentRepository;
        private final LectureRepository lectureRepository;


        public void dbInit1(){
            Student student = studentRepository.findByNumber("2019203029");
            Lecture lecture1 = lectureRepository.findOne(247L);
            Lecture lecture2 = lectureRepository.findOne(272L);
            Lecture lecture3 = lectureRepository.findOne(293L);
            Lecture lecture4 = lectureRepository.findOne(277L);

            StudentLecture st1 = StudentLecture.createStudentLecture(student, lecture1, "C0");
            StudentLecture st2 = StudentLecture.createStudentLecture(student, lecture2, "B+");
            StudentLecture st3 = StudentLecture.createStudentLecture(student, lecture3, "A0");
            StudentLecture st4 = StudentLecture.createStudentLecture(student, lecture4, "A+");

            em.persist(st1);
            em.persist(st2);
            em.persist(st3);
            em.persist(st4);


//            List<Department> departments = departmentRepository.findByCollege("전자정보공과대학");
//
//            for (Department department : departments) {
//                GradCondition gradCondition = new GradCondition(department, 2018, 22, 24, 60, 54, null, 54, 21, 133);
//                em.persist(gradCondition);
//            }
//
//            Department department0 = departmentRepository.findByName("컴퓨터정보공학부");
//            GradCondition gradCondition00 = new GradCondition(department0, 2018, 22, 24, 60, 54, null, 54, 21, 133);
//
//            Department department1 = departmentRepository.findByName("소프트웨어학부");
//            GradCondition gradCondition10 = new GradCondition(department1, 2018, 22, 21, 60, 54, null, 54, 21, 133);
//
//            Department department2 = departmentRepository.findByName("정보융합학부");
//            GradCondition gradCondition20 = new GradCondition(department2, 2018, 22, 9, 60, 54, null, 54, 21, 133);
//
//            Department department3 = departmentRepository.findByName("건축공학과");
//            GradCondition gradCondition30 = new GradCondition(department3, 2018, 22, 24, 60, 54, null, 54, 21, 133);
//
//            Department department4 = departmentRepository.findByName("화학공학과");
//            GradCondition gradCondition40 = new GradCondition(department4, 2018, 22, 24, 60, 54, null, 54, 21, 133);
//
//            Department department5 = departmentRepository.findByName("환경공학과");
//            GradCondition gradCondition50 = new GradCondition(department5, 2018, 22, 24, 60, 54, null, 54, 21, 133);
//
//            Department department6 = departmentRepository.findByName("건축학과");
//            GradCondition gradCondition60 = new GradCondition(department6, 2018, 22, null, 120, 120, null, 99, 45, 163);
//
//            Department department7 = departmentRepository.findByName("수학과");
//            GradCondition gradCondition70 = new GradCondition(department7, 2018, 16, null, 54, 45, 70, 45, 21, 133);
//
//            Department department8 = departmentRepository.findByName("전자바이오물리학과");
//            GradCondition gradCondition80 = new GradCondition(department8, 2018, 22, 16, 60, 54, 70, 54, 21, 133);
//
//            Department department9 = departmentRepository.findByName("화학과");
//            GradCondition gradCondition90 = new GradCondition(department9, 2018, 22, null, 60, 54, 70, 54, 21, 133);
//
//            Department department10 = departmentRepository.findByName("생할체육학과");
//            GradCondition gradCondition100 = new GradCondition(department10, 2018, 22, null, 60, 54, 70, 54, 21, 133);
//
//            Department department11 = departmentRepository.findByName("정보콘텐츠학과");
//            GradCondition gradCondition110 = new GradCondition(department11, 2018, 30, null, 45, 39, 60, 39, 21, 120);
//
//            Department department12 = departmentRepository.findByName("국어국문학과");
//            GradCondition gradCondition120 = new GradCondition(department12, 2018, 22, null, 51, 45, 70, 45, 21, 130);
//
//            Department department13 = departmentRepository.findByName("영어산업학과");
//            GradCondition gradCondition130 = new GradCondition(department13, 2018, 22, null, 45, 45, 70, 45, 21, 130);
//
//            Department department14 = departmentRepository.findByName("산업심리학과");
//            GradCondition gradCondition140 = new GradCondition(department14, 2018, 22, null, 51, 45, 66, 45, 21, 130);
//
//            Department department15 = departmentRepository.findByName("미디어영상학부");
//            GradCondition gradCondition150 = new GradCondition(department15, 2018, 22, null, 45, 45, 70, 45, 21, 130);
//
//            Department department16 = departmentRepository.findByName("동북아문화산업학부");
//            GradCondition gradCondition160 = new GradCondition(department16, 2018, 22, null, 66, 45, null, 45, 21, 130);
//
//            Department department17 = departmentRepository.findByName("행정학과");
//            GradCondition gradCondition170 = new GradCondition(department17, 2018, 22, null, 45, 45, 60, 45, 30, 130);
//
//            Department department18 = departmentRepository.findByName("법학부");
//            GradCondition gradCondition180 = new GradCondition(department18, 2018, 22, null, 45, 36, 60, 36, 21, 130);
//
//            Department department19 = departmentRepository.findByName("자산관리학과");
//            GradCondition gradCondition190 = new GradCondition(department19, 2018, 30, null, 45, 39, 60, 39, 21, 120);
//
//            Department department20 = departmentRepository.findByName("국제학부");
//            GradCondition gradCondition200 = new GradCondition(department20, 2018, 22, null, 45, 45, 60, 45, 21, 130);
//
//            Department department21 = departmentRepository.findByName("경영학부");
//            GradCondition gradCondition210 = new GradCondition(department21, 2018, 22, null, 45, 45, 70, 45, 21, 130);
//
//            Department department22 = departmentRepository.findByName("국제통상학부");
//            GradCondition gradCondition220 = new GradCondition(department22, 2018, 22, null, 57, 45, null, 45, 21, 130);
//
//            em.persist(gradCondition00);
//            em.persist(gradCondition10);
//            em.persist(gradCondition20);
//            em.persist(gradCondition30);
//            em.persist(gradCondition40);
//            em.persist(gradCondition50);
//            em.persist(gradCondition60);
//            em.persist(gradCondition70);
//            em.persist(gradCondition80);
//            em.persist(gradCondition90);
//            em.persist(gradCondition100);
//            em.persist(gradCondition110);
//            em.persist(gradCondition120);
//            em.persist(gradCondition130);
//            em.persist(gradCondition140);
//            em.persist(gradCondition150);
//            em.persist(gradCondition160);
//            em.persist(gradCondition170);
//            em.persist(gradCondition180);
//            em.persist(gradCondition190);
//            em.persist(gradCondition200);
//            em.persist(gradCondition210);
//            em.persist(gradCondition220);


        }

        private void persistLectures(Lecture... lectures) {
            for (Lecture lecture : lectures) {
                em.persist(lecture);
            }
        }

    }
}
