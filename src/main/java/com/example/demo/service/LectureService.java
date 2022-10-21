package com.example.demo.Service;

import com.example.demo.Repository.*;
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
    public Long addCustom(String name,
                          String professor,
                          String section,
                          String sectionDetail,
                          int credit,
                          int level,
                          String departmentName,
                          int yearOfLecture,
                          int semester){
        //엔티티 조회
        Lecture customLecture = Lecture.createLecture(name,professor,section,sectionDetail,credit,level,departmentName,yearOfLecture,semester);

        //커스텀 강의 추가
        Long customLectureId=lectureRepository.save(customLecture);

        return customLectureId;
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
