package com.example.demo.Service;

import com.example.demo.Repository.*;
import com.example.demo.controller.TimeTableController;
import com.example.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureService {
    //studentlectures 는 과거/현재 상관없이 메인시간표에 담긴 강의들

    private final LectureRepository lectureRepository;
    private final TimeSlotRepository timeSlotRepository;

    /**
     * 강의 검색
     */
    @Transactional
    public List<Lecture> findLectures(LectureSearch lectureSearch) {
        return lectureRepository.findAll(lectureSearch);
    }


    /**
     * 커스텀 강의 추가(누구의 커스텀인지는 어떻게 알지..? 변수 추가해야하나)
     */
    @Transactional
    public Long addCustom(Lecture lecture,List<TimeSlot> timeSlots){
        //엔티티 조회
        //Lecture customLecture = Lecture.createLecture(name,professor,section,sectionDetail,credit,level,departmentName,yearOfLecture,semester);

        //커스텀 강의 추가(생성 후 저장)
        Long customLectureId=lectureRepository.save(lecture);


        for (TimeSlot timeSlot : timeSlots) {
            //생성한 커스텀 강의에 있는 시간으로 TimeSlot 들 생성(추가)

//            TimeSlot.createTimeSlot(timeSlot.getDayName(),timeSlot.getStartTime(),timeSlot.getEndTime());
            timeSlotRepository.save(timeSlot); //TimeSlot 저장

            LectureTimeSlot lectureTimeSlot = LectureTimeSlot.createLectureTimeSlot(timeSlot); //LectureTimeSlot 생성
            lecture.addTimes(lectureTimeSlot); //LectureTimeSlot 생성(추가)
        }

        return customLectureId;
    }

    @Transactional
    public Long lectureTimeSlotSave(Lecture lecture,List<TimeSlot> timeSlots){
        //엔티티 조회
        //Lecture customLecture = Lecture.createLecture(name,professor,section,sectionDetail,credit,level,departmentName,yearOfLecture,semester);

        //커스텀 강의 추가(생성 후 저장)
        Long lectureId=lectureRepository.save(lecture);


        for (TimeSlot timeSlot : timeSlots) {
            //생성한 커스텀 강의에 있는 시간으로 TimeSlot 들 생성(추가)

//            TimeSlot.createTimeSlot(timeSlot.getDayName(),timeSlot.getStartTime(),timeSlot.getEndTime());
            timeSlotRepository.save(timeSlot); //TimeSlot 저장

            LectureTimeSlot lectureTimeSlot = LectureTimeSlot.createLectureTimeSlot(timeSlot); //LectureTimeSlot 생성
            lecture.addTimes(lectureTimeSlot); //LectureTimeSlot 생성(추가)
        }

        return lectureId;
    }

    /**
     * 커스텀 강의 삭제
     */
    @Transactional
    public void deleteCustom(Lecture lecture){
        if(lecture.isCustom()==true){
            lectureRepository.delete(lecture);
        }
    }

    /**
     * 해당 시간표와 요일, 시간이 겹치는 강의들 조회
     */
//    @Transactional
//    public List<Lecture> dupliSearch(Long timetableId, Long lectureId){
//        TimeTable timeTable = timeTableRepository.findOne(timetableId);
//        Lecture lecture = lectureRepository.findOne(lectureId);
//    }

    /**
     * 커스텀 강의 정보 변경(수정)=>편집버튼 누르면 수정됨
     */




}
