package com.example.demo.controller;

import com.example.demo.Repository.*;
import com.example.demo.Service.RecommendLectureService;
import com.example.demo.Service.TimeTableService;
import com.example.demo.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{studentNum}")
public class TimeTableController {

    private final TimeTableRepository timeTableRepository;
    private final LectureRepository lectureRepository;
    private final TimeTableLectureRepository timeTableLectureRepository;
    private final StudentLectureRepository studentLectureRepository;
    private final StudentRepository studentRepository;
    private final TimeTableService timeTableService;

    /**
     * 시간표 페이지 접속 시 & 학년 학기 선택시
     * @param number
     * @param year
     * @param semester
     * @return
     */
    @GetMapping("/timetablesAndLectues")
    public tablesAndLectureResult timeTableList(@PathVariable String number, int year, int semester){
        Student student = studentRepository.findByNumber(number);
        List<TimeTable> timeTables = timeTableRepository.findByStudentAndYearAndSemester(student,year,semester);
        List <TimeTableDto> timeTableList=timeTables.stream()
                .map(timeTable -> new TimeTableDto(timeTable))
                .collect(Collectors.toList());

        TimeTable PrimaryTimeTable = timeTableRepository.findByStudentAndYearAndSemesterAndPrimary(student,year,semester,true);
        List<MyLectureDto> lectureLists = PrimaryTimeTable.getLectures().stream()
                .map(timeTableLecture -> new MyLectureDto(timeTableLecture.getLecture()))
                .collect(Collectors.toList());

        return new tablesAndLectureResult(timeTableList,lectureLists);
    }

    /**
     * 시간표 추가 시
     */
    @PostMapping("/timetables/{year}_{semester}")
    public String addTimeTable(String number,@PathVariable("year") int year, @PathVariable("semester") int semester){
        timeTableService.addTimeTable(number,year,semester);
        return "";
    }


    @Data
    @AllArgsConstructor
    static class tablesAndLectureResult<A,B>{
        private A tableListDto;
        private B MyLectureListDto;

    }

    @Data
    @AllArgsConstructor
    static class TimeTableDto {
        private String tableName;
        private boolean isPrimary;

        public TimeTableDto(TimeTable timeTable) {
            tableName=timeTable.getTableName();
            isPrimary= timeTable.isPrimary();;
        }
    }

    @Data
    @AllArgsConstructor
    static class MyLectureDto {
        private String name;
        private String professor;
        private String section;
        private String sectionDetail;
        private int credit;
        private int level;
        private String departmentName;
        private String notes;

        public MyLectureDto(Lecture lecture) {
            name=lecture.getName();
            professor=lecture.getSection();
            section=lecture.getSection();
            sectionDetail=lecture.getSectionDetail();
            credit=lecture.getCredit();
            level=lecture.getLevel();
            departmentName=lecture.getDepartmentName();
            notes=lecture.getNotes();
        }
    }
}
