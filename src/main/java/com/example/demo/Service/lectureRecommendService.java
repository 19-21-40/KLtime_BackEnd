package com.example.demo.Service;

import com.example.demo.Repository.LectureRepository;
import com.example.demo.Repository.StudentLectureRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.domain.Lecture;
import com.example.demo.domain.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class lectureRecommendService {

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final StudentLectureRepository studentLectureRepository;


    /**
     * 이 과목을 들은 학생들이 가장 많이 들은 과목
     */
    @Transactional
    public List<String> lecRecom1(String lectureName){
        //엔티티 조회
        studentLectureRepository.recommendLectureNameList(lectureName).ifPresent(
                System.out::println
        );
        return studentLectureRepository.recommendLectureNameList(lectureName).orElse(new ArrayList<>());
    }

    /**
     * 이 과목을 들은 소프트웨어학부 n학년이 가장 많이 들은 과목 (n = 해당 사용자의 학년)
     */
    @Transactional
    public void lecRecom2(Lecture lecture, List<TimeSlot> timeSlots){
        //엔티티 조회
    }

}
