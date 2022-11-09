package com.example.demo.Service;

import com.example.demo.Repository.*;
import com.example.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureService {
    //studentlectures 는 과거/현재 상관없이 메인시간표에 담긴 강의들

    private final LectureRepository lectureRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final StudentRepository studentRepository;
    private final LectureTimeSlotRepository lectureTimeSlotRepository;

    /**
     * 강의 검색
     */
    @Transactional
    public List<Lecture> findLectures(LectureSearch lectureSearch) {
        return lectureRepository.findAll(lectureSearch);
    }

    /**
     * 커스텀 강의 학정번호 생성
     */
    @Transactional
    public String createCustomNum(){
        //엔티티 조회
        List<Lecture> CustomLectureList = lectureRepository.findByCustom(true);

        //커스텀강의 학정번호 생성
        int customLectureSize = CustomLectureList.size() + 1;

        String customLectureNum= "C_" + customLectureSize;
        for (Lecture lecture : CustomLectureList) {
            if (Objects.equals(lecture.getLectureNumber(), customLectureNum)){
                customLectureNum = "C_" + (customLectureSize+1);
            }
        }
        return customLectureNum;
    }


    /**
     * 커스텀 강의 추가 (애초에 LectureName/LectureTimeSlot 빼고 나머지는 다 null로 생성됨)
     * 넘어오는 lecture 는 lectureDto -> lecture (이미 이때 커스텀 강의 생성되어 있고, 여기선 save 해주는 것)
     */
    @Transactional
    public Long addCustom(Lecture lecture,List<TimeSlot> timeSlots){
        //엔티티 조회
        //Lecture customLecture = Lecture.createLecture(name,professor,section,sectionDetail,credit,level,departmentName,yearOfLecture,semester);

        //커스텀 강의 학정번호 설정 (조건문 추가)
        if(lecture.getLectureNumber()==null){
            lecture.setLectureNumber(createCustomNum());
        }

        //커스텀 강의 추가(생성 후 저장)
        Long customLectureId=lectureRepository.save(lecture);

        //TimeSlot 저장
        for(int i=0;i<timeSlots.size();i++){
            timeSlotRepository.save(timeSlots.get(i));
//            lecture.addTimes(lecture.getTimes().get(i)); //LectureTimeSlot 생성(추가)
        }

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
     * 커스텀 강의 정보 변경(수정) => 편집버튼 누르면 수정됨
     */
    @Transactional
    public void updateLectureInfo(String lectureNum, int year, String semester, String lectureName, List<TimeSlot> updateTimeSlots){
        //엔티티 조회
        Lecture lecture = lectureRepository.findByLectureNumAndYearAndSemesterWithTimes(lectureNum,year,semester);

        //커스텀 강의인 경우에만 강의 정보 변경 가능
        if(lecture.isCustom()){
            //=====강의명 변경 (강의명, 시간만 수정) =====//
            lecture.setName(lectureName); //강의명 변경
            //lectureRepository.save(lecture); //강의 정보 저장
            System.out.println("lectureName : "+lecture.getName());


            //===== 강의 시간 변경 =====//
            System.out.println("lectureTimeSlot 개수 : "+lecture.getTimes().size());
            //기존 lectureTimeSlot 들 삭제
//            for(int i=0;i<lecture.getTimes().size();i++){
//                System.out.println("lectureTimeSlot: "+lecture.getTimes().get(i));
//                lectureTimeSlotRepository.delete(lecture.getTimes().get(i));
//                lecture.getTimes().remove(i);
//            }
            lecture.getTimes().clear();
            //인자의 timeslot 들로 새 timeslot 생성
//            List<TimeSlot> updateTimeSlots=timeSlots.stream().map(timeSlot -> {
//                return timeSlotRepository.findByTimeSlot(timeSlot.getDayName(),timeSlot.getStartTime(),timeSlot.getEndTime())
//                        .orElseGet(()->{
//                            return TimeSlot.createTimeSlot(timeSlot.getDayName(),timeSlot.getStartTime(),timeSlot.getEndTime());
//                        });
//            }).collect(Collectors.toList());

            //새 lectureTimeSlot 들 생성
            List<LectureTimeSlot> newLectureTimeSlot = updateTimeSlots.stream().map(timeSlot -> LectureTimeSlot.createLectureTimeSlot(timeSlot)).collect(Collectors.toList());

            //새 lectureTimeSlot 추가
            for(int i=0;i<newLectureTimeSlot.size();i++){
                lecture.addTimes(newLectureTimeSlot.get(i));
            }

            //새 timeSlot 들 저장
            for(int i=0;i<updateTimeSlots.size();i++){
                timeSlotRepository.save(updateTimeSlots.get(i));
            }
        }
    }
}
