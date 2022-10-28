package com.example.demo.controller;

import com.example.demo.Repository.*;
import com.example.demo.Service.RecommendLectureService;
import com.example.demo.Service.TimeTableService;
import com.example.demo.domain.*;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/timetable/{year}/{semester}","api/timetable/2022/1학기"})
public class TimeTableController {

    private final TimeTableRepository timeTableRepository;
    private final LectureRepository lectureRepository;
    private final TimeTableLectureRepository timeTableLectureRepository;
    private final StudentLectureRepository studentLectureRepository;
    private final StudentRepository studentRepository;
    private final TimeTableService timeTableService;

    //lecture domain 수정하고 난뒤에, 컨트롤러 3개의 LectureDTO 의 스펙을 수정해야함.
    //테이블리스트 안의 테이블들의 강의리스트들을 같이 받도록 수정해야함

    /**
     * 시간표 페이지 처음 접속 시 & 년도/학기 변경(선택)시 (해당 년도/학기의 기본시간표가 떠야 함)
     * @param studentDTO
     * @param year (default = 2022)
     * @param semester (default = 2)
     * @return ResponseEntity.ok().body(new TimeTableController.TableResult(timeTableList));
     */
    @PostMapping("/main")
    public ResponseEntity<?> totaltimeTableList(
            @RequestBody StudentDTO studentDTO,
            //@PathVariable String number,
            @PathVariable(value = "year", required = false) @DefaultValue() int year,
            @PathVariable(value = "semester", required = false) String semester
    ){
        try {
            if (studentDTO.getToken() != null) {
                Student student = studentRepository.findByNumber(studentDTO.getNumber());
                List<TimeTable> timeTables = timeTableRepository.findByStudentAndYearAndSemester(student,year,semester);
                List <TimeTableDto> timeTableList=timeTables.stream()
                        .map(timeTable -> new TimeTableDto(timeTable))
                        .collect(Collectors.toList());

//                TimeTable PrimaryTimeTable = timeTableRepository.findByStudentAndYearAndSemesterAndPrimary(student,year,semester,true);
//                List<MyLectureDto> lectureLists = PrimaryTimeTable.getLectures().stream()
//                        .map(timeTableLecture -> new MyLectureDto(timeTableLecture.getLecture()))
//                        .collect(Collectors.toList());

                //return ResponseEntity.ok().body(new TimeTableController.tablesAndLectureResult(timeTableList,lectureLists));
                return ResponseEntity.ok().body(new TimeTableController.TableResult(timeTableList));

            }else{
                throw new IllegalStateException("토큰이 존재하지 않습니다.");
            }
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    //    @GetMapping("/timetablesAndLectures")
//    public tablesAndLectureResult timeTableList(@PathVariable String number, int year, String semester){
//        Student student = studentRepository.findByNumber(number);
//        List<TimeTable> timeTables = timeTableRepository.findByStudentAndYearAndSemester(student,year,semester);
//        List <TimeTableDto> timeTableList=timeTables.stream()
//                .map(timeTable -> new TimeTableDto(timeTable))
//                .collect(Collectors.toList());
//
//        TimeTable PrimaryTimeTable = timeTableRepository.findByStudentAndYearAndSemesterAndPrimary(student,year,semester,true);
//        List<MyLectureDto> lectureLists = PrimaryTimeTable.getLectures().stream()
//                .map(timeTableLecture -> new MyLectureDto(timeTableLecture.getLecture()))
//                .collect(Collectors.toList());
//
//        return new tablesAndLectureResult(timeTableList,lectureLists);
//    }

    /**
     * 해당 년도/학기 시간표 추가 (자동으로 이름 생성) -> 쿼리 확인
     * @param studentDTO
     * @param year
     * @param semester
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/add")
    public ResponseEntity<?> addTimeTable(
            @RequestBody StudentDTO studentDTO,
            //@PathVariable String number,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester
    ){
        try {
            if (studentDTO.getToken() != null) {
                //Student student = studentRepository.findByNumber(studentDTO.getNumber());
                timeTableService.addTimeTable(studentDTO.getNumber(),year,semester);

                return new ResponseEntity<>(HttpStatus.OK); //시간표 추가 후 OK 상태 반환
            }else{
                throw new IllegalStateException("토큰이 존재하지 않습니다.");
            }
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 해당 년도/학기 시간표 삭제
     * @param studentDTO
     * @param year
     * @param semester
     * @param tableName
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/delete/{tableName}")
    public ResponseEntity<?> deleteTimeTable(
            @RequestBody StudentDTO studentDTO,
            //@PathVariable String number,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "tableName") String tableName
    ){
        try {
            if (studentDTO.getToken() != null) {
                Student student = studentRepository.findByNumber(studentDTO.getNumber());
                timeTableService.deleteTimeTable(student.getNumber(),year,semester,tableName);

                return new ResponseEntity<>(HttpStatus.OK); //시간표 삭제 후 OK 상태 반환
            }else{
                throw new IllegalStateException("토큰이 존재하지 않습니다.");
            }
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 해당 시간표에서 수정하기 버튼 눌렀을 때
     * @param studentDTO
     * @param year
     * @param semester
     * @return tablesAndLectureResult(timeTableList,lectureLists)
     */
    @PostMapping("/update")
    public ResponseEntity<?> editTimeTable(
            @RequestBody StudentDTO studentDTO,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester
    ){
        try {
            if (studentDTO.getToken() != null) {
                //Student student = studentRepository.findByNumber(studentDTO.getNumber());
                LectureSearch lectureSearch = new LectureSearch();
                lectureSearch.setYearOfLecture(year);
                lectureSearch.setSemester(semester);
                List<Lecture> lectures = lectureRepository.findAll(lectureSearch);
                
                List<LectureDto> searchLectureLists = lectures.stream()
                        .map(searchlecture -> new LectureDto(searchlecture))
                        .collect(Collectors.toList());

                return ResponseEntity.ok().body(new LectureResult<>(searchLectureLists));
            }else{
                throw new IllegalStateException("토큰이 존재하지 않습니다.");
            }
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 해당 년도/학기 기본 시간표 변경 -> 쿼리 확인 (전/후 각각 한번씩 select)
     * @param studentDTO
     * @param year
     * @param semester
     * @param newPriTableName
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/changePrimary/{tableName}")
    public ResponseEntity<?> changePrimaryTimeTable(
            @RequestBody StudentDTO studentDTO,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "tableName") String newPriTableName
    ){
        try {
            if (studentDTO.getToken() != null) {
                Student student = studentRepository.findByNumber(studentDTO.getNumber());
                timeTableService.changePrimary(student,year,semester,newPriTableName);

                return new ResponseEntity<>(HttpStatus.OK); //기본 시간표 변경 후 OK 상태 반환
            }else{
                throw new IllegalStateException("토큰이 존재하지 않습니다.");
            }
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 해당 년도/학기/시간표 이름 변경 -> 쿼리 확인 (+ 중복 확인 select)
     * @param studentDTO
     * @param year
     * @param semester
     * @param oldTableName
     * @param newTableName
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/changeName/{oldTableName}/{newTableName}")
    public ResponseEntity<?> changeTimeTableName(
            @RequestBody StudentDTO studentDTO,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "oldTableName") String oldTableName,
            @PathVariable(value = "newTableName") String newTableName
    ){
        try {
            if (studentDTO.getToken() != null) {
                //Student student = studentRepository.findByNumber(studentDTO.getNumber());
                timeTableService.changeTimeTableName(studentDTO.getNumber(), year,semester,oldTableName,newTableName);

                return new ResponseEntity<>(HttpStatus.OK); //시간표 이름 변경 후 OK 상태 반환
            }else{
                throw new IllegalStateException("토큰이 존재하지 않습니다.");
            }
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 해당 년도/학기/시간표 내 강의 추가 (커스텀 X) -> 쿼리 확인
     * @param studentDTO
     * @param year
     * @param semester
     * @param tableName
     * @param lectureNum
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/addLecture/{tableName}/{lectureNum}")
    public ResponseEntity<?> addLecture(
            @RequestBody StudentDTO studentDTO,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "tableName") String tableName,
            @PathVariable(value = "lectureNum") String lectureNum
            ){
        try {
            if (studentDTO.getToken() != null) {
                //Student student = studentRepository.findByNumber(studentDTO.getNumber());
                Lecture lecture = lectureRepository.findByLectureNum(lectureNum);
                timeTableService.addLecture(studentDTO.getNumber(),year,semester,tableName,lecture);

                return new ResponseEntity<>(HttpStatus.OK); //시간표 내 강의 추가 후 OK 상태 반환
            }else{
                throw new IllegalStateException("토큰이 존재하지 않습니다.");
            }
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 해당 년도/학기/시간표 내 커스텀 강의 추가 (커스텀 O ) -> 쿼리 확인
     * @param studentAndCustomResult
     * @param year
     * @param semester
     * @param tableName
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/addCustomLecture/{tableName}")
    public ResponseEntity<?> addCustomLecture(
            @RequestBody StudentAndCustomResult<StudentDTO, LectureDto> studentAndCustomResult,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "tableName") String tableName
    ){
        try {
            if (studentAndCustomResult.studentDto.getToken() != null) {
                //Student student = studentRepository.findByNumber(studentDTO.getNumber());
                Lecture lecture = Lecture.from(studentAndCustomResult.customLectureDto).
                        orElseThrow(()->{throw new IllegalStateException("지정된 형식과 일치하지 않습니다.");});
                timeTableService.addCustomLecture(studentAndCustomResult.studentDto.getNumber(),year,semester,tableName,lecture);

                return new ResponseEntity<>(HttpStatus.OK); //시간표 내 강의 추가 후 OK 상태 반환
            }else{
                throw new IllegalStateException("토큰이 존재하지 않습니다.");
            }
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 해당 년도/학기/시간표 내 커스텀 강의 추가 (커스텀 O ) -> 쿼리 확인
     * @param studentAndCustomResult
     * @param year
     * @param semester
     * @param tableName
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
//    @PostMapping("/deleteLecture/{tableName}")
//    public ResponseEntity<?> deleteLecture(
//            @RequestBody StudentAndCustomResult<StudentDTO, LectureDto> studentAndCustomResult,
//            @PathVariable(value = "year") int year,
//            @PathVariable(value = "semester") String semester,
//            @PathVariable(value = "tableName") String tableName
//    ){
//        try {
//            if (studentAndCustomResult.studentDto.getToken() != null) {
//                //Student student = studentRepository.findByNumber(studentDTO.getNumber());
//                Lecture lecture = Lecture.from(studentAndCustomResult.customLectureDto).
//                        orElseThrow(()->{throw new IllegalStateException("지정된 형식과 일치하지 않습니다.");});
//                timeTableService.addCustomLecture(studentAndCustomResult.studentDto.getNumber(),year,semester,tableName,lecture);
//
//                return new ResponseEntity<>(HttpStatus.OK); //시간표 내 강의 추가 후 OK 상태 반환
//            }else{
//                throw new IllegalStateException("토큰이 존재하지 않습니다.");
//            }
//        }catch (Exception e){
//            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
//                    .error(e.getMessage())
//                    .build();
//            return ResponseEntity.badRequest().body(responseDTO);
//        }
//    }


    //======================================LectureDto, TimeTableDto====================================//

    @Data
    @AllArgsConstructor
    static class StudentAndCustomResult<A,B>{
        private A studentDto;
        private B customLectureDto;
    }

    @Data
    @AllArgsConstructor
    static class LectureResult<T>{
        private T lectureList;
    }

    @Data
    @AllArgsConstructor
    static class TableResult<T>{
        private T totaltableList;
    }

    @Data
    @AllArgsConstructor
    static class TimeTableDto {
        private String tableName;
        private boolean isPrimary;

        //렉처리스트 추가(수연)
        private List<TimeTableLecture> myLectureList;

        public TimeTableDto(TimeTable timeTable) {
            tableName=timeTable.getTableName();
            isPrimary= timeTable.isPrimary();
            myLectureList=timeTable.getLectures();
        }
    }

    @Data
    @AllArgsConstructor
    public static class LectureDto {
        private String name;
        private String professor;
        private String section;
        private String sectionDetail;
        private int credit;
        private int level;
        private String departmentName;
        private String notes;
        private int yearOfLecture;
        private String semester;
        private boolean isCustom;
        private List<LectureTimeSlot> times = new ArrayList<>();

        public LectureDto(Lecture lecture) {
            name=lecture.getName();
            professor=lecture.getSection();
            section=lecture.getSection();
            sectionDetail=lecture.getSectionDetail();
            credit=lecture.getCredit();
            level=lecture.getLevel();
            departmentName=lecture.getDepartmentName();
            notes=lecture.getNotes();
            yearOfLecture=lecture.getYearOfLecture();
            semester=lecture.getSemester();
            times=lecture.getTimes();//??


        }
    }
}
