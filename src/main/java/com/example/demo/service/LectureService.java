package com.example.demo.Service;

import com.example.demo.Repository.*;
import com.example.demo.domain.Lecture;
import com.example.demo.domain.Student;
import com.example.demo.domain.TimeTable;
import com.example.demo.domain.TimeTableLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final TimeTableRepository timeTableRepository;
    private final TimeSlotRepository timeSlotRepository;

    /**
     * 강의 검색
     */
    @Transactional
    public List<Lecture> findLectures(LectureSearch lectureSearch) {
        return lectureRepository.findAll(lectureSearch);
    }

    /**
     * 시간표에서 강의 한개 추가(커스텀 포함)
     */
    @Transactional
    public void addLecture(Long studentId, Long timetableId, Long lectureId) {
        //엔티티 조회
        Student student = studentRepository.findById(studentId);
        TimeTable timeTable = timeTableRepository.findOne(timetableId);
        Lecture lecture = lectureRepository.findOne(lectureId);
        TimeTableLecture timeTableLecture = TimeTableLecture.createTimeTableLecture(lecture);

        //시간표강의 생성(강의 추가)
        timeTable.addTimeTableLecture(timeTableLecture);
    }

    /**
     * 시간표에서 강의 삭제(커스텀 포함)
     */
    @Transactional
    public void deleteLecture(Long timetableId, Long lectureId, TimeTableLecture timetableLecture) {
        //엔티티 조회
        TimeTable timeTable = timeTableRepository.findOne(timetableId);
        Lecture lecture = lectureRepository.findOne(lectureId);

        //시간표에서 강의 삭제
        lecture.delete(timetableLecture);
    }

    /**
     * 커스텀 강의 추가
     */
//    @Transactional
//    public void addCustom(Long timetableId){
//        //엔티티 조회
//        TimeTable timeTable = timeTableRepository.findOne(timetableId);
//
//
//    }

    /**
     * 커스텀 강의 삭제
     */
//    @Transactional

    /**
     * 해당 시간표와 요일, 시간이 겹치는 강의들 조회
     */
//    @Transactional
//    public List<Lecture> dupliSearch(Long timetableId, Long lectureId){
//        TimeTable timeTable = timeTableRepository.findOne(timetableId);
//        Lecture lecture = lectureRepository.findOne(lectureId);
//    }

}
