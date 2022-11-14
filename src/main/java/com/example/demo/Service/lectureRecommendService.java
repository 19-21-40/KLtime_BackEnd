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
     * 이 과목을 들은 학생들이 가장 많이 들은 과목 top3
     */
    @Transactional
    public List<Lecture> lecRecom1(Lecture lecture){
        //엔티티 조회
//        studentLectureRepository.recommendLectureNameList(lectureName).ifPresent(
//                System.out::println
//        );
//        return studentLectureRepository.recommendLectureNameList(lectureName).orElse(new ArrayList<>());
        studentLectureRepository.recommendLectureList1(lecture).ifPresent(
                System.out::println
        );
        return studentLectureRepository.recommendLectureList1(lecture).orElse(new ArrayList<>());
    }

    /**
     * 이 과목을 들은 소프트웨어학부 n학년이 가장 많이 들은 과목 top3 (n = 해당 사용자의 학년)
     */
    @Transactional
    public List<Lecture> lecRecom2(Lecture lecture, int grade){
        //엔티티 조회
        studentLectureRepository.recommendLectureList2(lecture, grade).ifPresent(
                System.out::println
        );
        return studentLectureRepository.recommendLectureList2(lecture, grade).orElse(new ArrayList<>());
    }

}
