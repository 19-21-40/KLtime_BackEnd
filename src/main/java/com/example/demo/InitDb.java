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
import java.util.Optional;


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
            Department department = departmentRepository.findByName("소프트웨어학부").get();
            List<Lecture> resultList = lectureRepository.findByDepartment2022(department);

            System.out.println("강의 수 : " + resultList.size());
            for (Lecture lecture : resultList) {
                System.out.println("학정번호 : " + lecture.getLectureNumber() + ", 강의명 : " + lecture.getName() + ", category : " + lecture.getCategory());
            }
        }

        private void persistLectures(Lecture... lectures) {
            for (Lecture lecture : lectures) {
                em.persist(lecture);
            }
        }

    }
}
