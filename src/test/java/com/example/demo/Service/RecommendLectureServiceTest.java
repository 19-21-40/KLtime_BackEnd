package com.example.demo.Service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@RunWith(SpringRunner.class)
//@Transactional
public class RecommendLectureServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired RecommendLectureService recommendLectureService;

    @Test
    public void recommendTest() throws Exception {
        //given

        recommendLectureService.recommendLecture();

        //when

        //then
    }
}