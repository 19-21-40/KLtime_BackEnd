package com.example.demo.controller;

import com.example.demo.Repository.LectureRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.Service.GraduationRequirementService;
import com.example.demo.Service.RecommendLectureService;
import com.example.demo.domain.Lecture;
import com.example.demo.domain.Student;
import com.example.demo.domain.TimeSlot;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend/{year}/{semester}")
public class RecommendController {

    final private StudentRepository studentRepository;
    final private RecommendLectureService recommendLectureService;
    final private LectureRepository lectureRepository;

    /**
     * 이 과목을 들은 학생들이 가장 많이 들은 과목 top3 -> 쿼리 확인
     * @param id
     * @param year
     * @param semester
     * @param lectureNum
     * @return
     */
    @PostMapping("/lectureList1/{lectureNum}")
    public ResponseEntity<?> recommendLectureList1(
            @AuthenticationPrincipal Long id,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "lectureNum") String lectureNum
    ){
        try{
            Lecture lecture = lectureRepository.findByLectureNumAndYearAndSemester(lectureNum,year,semester);

            List<Lecture> lectureList = recommendLectureService.lecRecom1(lecture);
            List<LectureDto> lectureListDto = lectureList.stream().limit(3).map(Lecture -> {
                //외국인만 수강가능하거나, 1학년만 수강이 가능한 경우엔 추천 리스트에서 제외
                if(!Lecture.getNotes().contains("외국인")&&!Lecture.getNotes().contains("1학년")){
                    return new LectureDto(Lecture);
                }
                else{
                    return null;
                }
            }).collect(toList());

            return ResponseEntity.ok().body(new LectureResult(lectureListDto));
        }
        catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 이 과목을 들은 소프트웨어학부 n학년이 가장 많이 들은 과목 top3 (n = 해당 사용자의 학년) -> 쿼리 확인
     * @param id
     * @param year
     * @param semester
     * @param lectureNum
     * @return
     */
    @PostMapping("/lectureList2/{lectureNum}")
    public ResponseEntity<?> recommendLectureList2(
            @AuthenticationPrincipal Long id,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "lectureNum") String lectureNum
    ){
        try {
            Student student = studentRepository.findById(id);
            Lecture lecture = lectureRepository.findByLectureNumAndYearAndSemester(lectureNum,year,semester);

            List<Lecture> lectureList = recommendLectureService.lecRecom2(lecture,student.getDepartment().getName(),student.getGrade());
            List<LectureDto> lectureListDto = lectureList.stream().limit(3).map(Lecture -> {
                //외국인만 수강가능하거나, 1학년만 수강이 가능한 경우엔 추천 리스트에서 제외
                if(!Lecture.getNotes().contains("외국인")&&!Lecture.getNotes().contains("1학년")){
                    return new LectureDto(Lecture);
                }
                else{
                    return null;
                }
            }).collect(toList());

            return ResponseEntity.ok().body(new LectureResult(lectureListDto));
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @Data
    @AllArgsConstructor
    static class LectureResult<T>{
        private T lectureList;
    }

    @Data
    @AllArgsConstructor
    public static class TimeSlotDto{
        private String day;
        private String startTime;
        private String endTime;

        public TimeSlotDto(TimeSlot timeSlot){
            day =timeSlot.getDayName();
            startTime=timeSlot.getStartTime();
            endTime=timeSlot.getEndTime();
        }
    }

    @Data
    @AllArgsConstructor
    public static class LectureDto {
        private String id;
        private String lectureName;
        private String professor;
        private String section;
        private String sectionDetail;
        private int credit;
        private int level;
        private String department;
        private String notes;
        private int yearOfLecture;
        private String semester;
        private List<TimeSlotDto> lectureTimes = new ArrayList<>(); //추가

        public LectureDto(Lecture lecture) {
            id=lecture.getLectureNumber();
            lectureName=lecture.getName();
            professor=lecture.getProfessor();
            section=lecture.getSection();
            sectionDetail=lecture.getSectionDetail();
            credit=lecture.getCredit();
            level=lecture.getLevel();
            department=lecture.getCategory();
            notes=lecture.getNotes();
            yearOfLecture=lecture.getYearOfLecture();
            semester=lecture.getSemester();
            lectureTimes =lecture.getTimes().stream().map(lectureTimeSlot -> new TimeSlotDto(lectureTimeSlot.getTimeSlot())).collect(Collectors.toList());
        }
    }
}
