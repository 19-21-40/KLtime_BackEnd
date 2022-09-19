package com.example.demo.Service;

import com.example.demo.Repository.LectureRepository;
import com.example.demo.Repository.LectureSearch;
import com.example.demo.Repository.TimeTableLectureRepository;
import com.example.demo.Repository.TimeTableRepository;
import com.example.demo.domain.Lecture;
import com.example.demo.domain.TimeTable;
import com.example.demo.domain.TimeTableLecture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class LectureServiceTest {

    @Autowired LectureService lectureService;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    TimeTableRepository timeTableRepository;
    @Autowired
    TimeTableLectureRepository timeTableLectureRepository;

    @Test
    @Rollback(false)
    public void 강의_검색() throws Exception {
        //given
        LectureSearch lectureSearch = new LectureSearch();
        lectureSearch.setDepartmentName("소프트웨어학부");
        //when
        List<Lecture> lectures = lectureService.findLectures(lectureSearch);
        //then
        assertEquals("강의 검색",9,lectures.size());
    }

    @Test
    @Rollback(false)
    public void 강의_추가() throws Exception {
        //given
        TimeTable timeTable1 =  timeTableRepository.findOne(33L);
        Lecture lecture1 = lectureRepository.findOne(25L);
        //when
        lectureService.addLecture(5L,timeTable1.getId(),lecture1.getId());
        //then
        assertEquals("시간표에 강의 추가",3,timeTable1.getLectures().size());
    }

    @Test
    @Rollback(false)
    public void 강의_삭제() throws Exception {
        //given
        TimeTable timeTable1 =  timeTableRepository.findOne(33L);
        Lecture lecture1 = lectureRepository.findOne(25L);
        TimeTableLecture timeTableLecture = timeTableLectureRepository.findOne(32L);
        //when
        lectureService.deleteLecture(timeTable1.getId(), lecture1.getId(),timeTableLecture);
        //then
        assertEquals("시간표에 강의 추가",1,timeTable1.getLectures().size());
    }

}