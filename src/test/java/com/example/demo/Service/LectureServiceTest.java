package com.example.demo.Service;

import com.example.demo.Repository.*;
import com.example.demo.domain.Lecture;
import com.example.demo.domain.Student;
import com.example.demo.domain.TimeTable;
import com.example.demo.domain.TimeTableLecture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
    StudentRepository studentRepository;
    @Autowired
    EntityManager em;




}