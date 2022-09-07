package com.example.demo.Repository;

import com.example.demo.domain.Lecture;
import com.example.demo.domain.LectureTimeSlot;
import com.example.demo.domain.TimeSlot;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)

public class LectureRepositoryTest {

    @Autowired EntityManager em;

    @Autowired
    LectureRepository lectureRepository;


    @Test
//    @Rollback(value = false)
    public void save() throws Exception {
        //given
        TimeSlot timeSlot=TimeSlot.createTimeSlot("월","9:00","10:30");
        em.persist(timeSlot);
        LectureTimeSlot lts=LectureTimeSlot.createLectureTimeSlot(timeSlot);
        em.persist(lts);
        Lecture lecture=Lecture.createLecture(
                "H030-02-0819-01",
                "선형대수학",
                "김상목",
                "기선",
                "수학",
                3,
                2,
                "소프트웨어학부",
                2022,
                "1학기",
                "");
        lecture.addTimes(lts);

        lectureRepository.save(lecture);

        //when
        LectureSearch lectureSearch = new LectureSearch();
        lectureSearch.setLevel(3);
        List<Lecture> lectures = lectureRepository.findAll(lectureSearch);


        //then
            Assertions.assertEquals(lectures.contains(lecture),false);

    }


    @Test
    public void findAll() throws Exception {
        //given
        TimeSlot timeSlot=TimeSlot.createTimeSlot("월","9:00","10:30");
        em.persist(timeSlot);
        LectureTimeSlot lts=LectureTimeSlot.createLectureTimeSlot(timeSlot);
        em.persist(lts);
        Lecture lecture=Lecture.createLecture(
                "H030-02-0819-01",
                "선형대수학",
                "김상목",
                "기선",
                "수학",
                3,
                2,
                "소프트웨어학부",
                2022,
                "1학기",
                "");
        lecture.addTimes(lts);

        lectureRepository.save(lecture);

        //when
        LectureSearch lectureSearch = new LectureSearch();
        lectureSearch.setLevel(3);
        List<Lecture> lectures = lectureRepository.findAll(lectureSearch);


        //then
        Assertions.assertEquals(lectures.contains(lecture),false);

    }

    @Test
    public void findByTimeSlot() throws Exception {
        //given
        TimeSlot timeSlot1=TimeSlot.createTimeSlot("월","9:00","10:30");
        TimeSlot timeSlot2=TimeSlot.createTimeSlot("화","10:30","12:00");
        em.persist(timeSlot1);
        em.persist(timeSlot2);

        LectureTimeSlot lts1=LectureTimeSlot.createLectureTimeSlot(timeSlot1);
        LectureTimeSlot lts2=LectureTimeSlot.createLectureTimeSlot(timeSlot1);
        em.persist(lts1);
        em.persist(lts2);

        Lecture lecture=Lecture.createLecture(
                "H030-02-0819-01",
                "선형대수학",
                "김상목",
                "기선",
                "수학",
                3,
                2,
                "소프트웨어학부",
                2022,
                "1학기",
                "");
//        lecture.addTimes(lts1);
//        lecture.addTimes(lts2);

        lectureRepository.save(lecture);

        //when
        List<Lecture> findLecture = lectureRepository.findByTimeSlot(timeSlot1);

        //then
        Assertions.assertEquals(findLecture.contains(lecture),true);
    }
}