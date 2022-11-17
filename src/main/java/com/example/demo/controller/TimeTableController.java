package com.example.demo.controller;

import com.example.demo.Repository.*;
import com.example.demo.Service.LectureService;
import com.example.demo.Service.TimeTableService;
import com.example.demo.domain.*;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/timetable/{year}/{semester}")
public class TimeTableController {

    private final TimeTableRepository timeTableRepository;
    private final LectureRepository lectureRepository;
    private final TimeTableLectureRepository timeTableLectureRepository;
    private final StudentLectureRepository studentLectureRepository;
    private final StudentRepository studentRepository;
    private final TimeTableService timeTableService;
    private final TimeSlotRepository timeSlotRepository;

    private final LectureService lectureService;

    /**
     * 시간표 페이지 처음 접속 시 & 년도/학기 변경(선택)시 (해당 년도/학기의 기본시간표가 떠야 함) -> 쿼리 확인
     * @param
     * @param year
     * @param semester
     * @return ResponseEntity.ok().body(new TimeTableController.TableResult(timeTableList));
     */
    @GetMapping("/totalTimeTableList")
    public ResponseEntity<?> totalTimeTableList(
            @AuthenticationPrincipal Long id,
//            @RequestBody StudentDTO studentDTO,
            @PathVariable(value = "year", required = false) int year,
            @PathVariable(value = "semester", required = false) String semester
    ){
        try {
            Student student=studentRepository.findById(id);
//            Student student = studentRepository.findByNumber(studentDTO.getNumber());
            List<TimeTable> timeTables = timeTableRepository.findByStudentAndYearAndSemesterWithLecture(student,year,semester); //fetch outer join
//                List<TimeTable> timeTables = timeTableRepository.findByStudentAndYearAndSemester(student,year,semester);

            int newId = 1;
            List <TimeTableDto> timeTableList = new ArrayList<>();
            for (TimeTable timeTable : timeTables) {
                timeTableList.add(new TimeTableDto(newId++, timeTable));
            }

            return ResponseEntity.ok().body(new TableResult(timeTableList));
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 해당 년도/학기 시간표 추가 (자동으로 이름 생성) -> 쿼리 확인
     * @param
     * @param year
     * @param semester
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/add/{tableName}")
    public ResponseEntity<?> addTimeTable(
            @AuthenticationPrincipal Long id,
            //@PathVariable String number,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "tableName") String tableName
    ){
        try {
            Student student=studentRepository.findById(id);
//            Student student = studentRepository.findByNumber(studentDTO.getNumber());
            timeTableService.addTimeTable(student,tableName,year,semester);

            return new ResponseEntity<>(HttpStatus.OK); //시간표 추가 후 OK 상태 반환
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 해당 년도/학기 시간표 삭제 -> 쿼리 확인
     * @param
     * @param year
     * @param semester
     * @param tableName
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/delete/{tableName}")
    public ResponseEntity<?> deleteTimeTable(
            @AuthenticationPrincipal Long id,
            //@PathVariable String number,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "tableName") String tableName
    ){
        try {
            Student student=studentRepository.findById(id);
            timeTableService.deleteTimeTable(student.getNumber(),year,semester,tableName);

            return new ResponseEntity<>(HttpStatus.OK); //시간표 삭제 후 OK 상태 반환
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 해당 년도/학기 강의 리스트 -> 쿼리 확인
     * @param
     * @param year
     * @param semester
     * @return tablesAndLectureResult(timeTableList,lectureLists)
     */
    @GetMapping("/totalLectureList")
    public ResponseEntity<?> totalLectureList(
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester
    ){
        try {
            //Student student = studentRepository.findByNumber(studentDTO.getNumber());
            List<Lecture> lectures = lectureRepository.findByYearAndSemesterWithTimeslot(year,semester);

            List<LectureDto> searchLectureLists = lectures.stream()
                    .map(lecture -> new LectureDto(lecture))
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(new LectureResult<>(searchLectureLists));
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 해당 년도/학기 기본 시간표 변경 -> 쿼리 확인 (전/후 각각 한번씩 select)
     * @param
     * @param year
     * @param semester
     * @param newPriTableName
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/changePrimary/{tableName}")
    public ResponseEntity<?> changePrimaryTimeTable(
            @AuthenticationPrincipal Long id,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "tableName") String newPriTableName
    ){
        try {
            Student student=studentRepository.findById(id);
//            Student student = studentRepository.findByNumber(studentDTO.getNumber());
            timeTableService.changePrimary(student,year,semester,newPriTableName);

            return new ResponseEntity<>(HttpStatus.OK); //기본 시간표 변경 후 OK 상태 반환
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 해당 년도/학기/시간표 이름 변경 -> 쿼리 확인 (+ 중복 확인 select)
     * @param
     * @param year
     * @param semester
     * @param oldTableName
     * @param newTableName
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/changeName/{oldTableName}/{newTableName}")
    public ResponseEntity<?> changeTimeTableName(
            @AuthenticationPrincipal Long id,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "oldTableName") String oldTableName,
            @PathVariable(value = "newTableName") String newTableName
    ){
        try {
            Student student=studentRepository.findById(id);
//            Student student = studentRepository.findByNumber(studentDTO.getNumber());
            timeTableService.changeTimeTableName(student, year,semester,oldTableName,newTableName);

            return new ResponseEntity<>(HttpStatus.OK); //시간표 이름 변경 후 OK 상태 반환
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    
    //========== 강의 추가/삭제 ==========//
    /**
     * 해당 년도/학기/시간표 내 강의 추가 (커스텀 X) -> 쿼리 확인
     * @param year
     * @param semester
     * @param tableName
     * @param lectureNum
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/addLecture/{tableName}/{lectureNum}")
    public ResponseEntity<?> addLecture(
            @AuthenticationPrincipal Long id,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "tableName") String tableName,
            @PathVariable(value = "lectureNum") String lectureNum
    ){
        try {
            Student student=studentRepository.findById(id);
            //Student student = studentRepository.findByNumber(studentDTO.getNumber());
            Lecture lecture = lectureRepository.findByLectureNumAndYearAndSemester(lectureNum,year,semester);
            timeTableService.addLecture(student.getNumber(),year,semester,tableName,lecture);
            if(lecture.getNotes().contains("외국인")){
                return ResponseEntity.ok().body("외국인만 수강가능합니다.");
            }
            return ResponseEntity.ok().body("추가되었습니다."); //시간표 내 강의 추가 후 OK 상태 반환
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    /**
     * 해당 년도/학기/시간표 내 커스텀 강의 추가 (커스텀 O ) -> 쿼리 확인
     *
     * @param lectureAndTimeSlotResult
     * @param year
     * @param semester
     * @param tableName
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/addCustomLecture/{tableName}")
    public ResponseEntity<?> addCustomLecture(
            @AuthenticationPrincipal Long id,
            @RequestBody LectureAndTimeSlotResult<LectureDto, List<TimeSlotDto>> lectureAndTimeSlotResult,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "tableName") String tableName
    ) {
        try {
            Student student=studentRepository.findById(id);
            //Student student = studentRepository.findByNumber(studentDTO.getNumber());
            List<TimeSlot>timeSlots=lectureAndTimeSlotResult.timeSlotDtoList.stream().map((timeSlotDto)-> {
                return timeSlotRepository.findByTimeSlot(timeSlotDto.getDay(), timeSlotDto.getStartTime(), timeSlotDto.getEndTime())
                        .orElseGet(() -> {
                            return TimeSlot.from(timeSlotDto).orElseThrow(()->new IllegalStateException("error"));
                        });
            }).collect(Collectors.toList());
            Lecture lecture = Lecture.from(lectureAndTimeSlotResult.lectureDto,timeSlots).
                    orElseThrow(() -> {
                        throw new IllegalStateException("지정된 형식과 일치하지 않습니다.");
                    });
            timeTableService.addCustomLecture(student.getNumber(), year, semester, tableName, lecture, timeSlots);

            return new ResponseEntity<>(HttpStatus.OK); //시간표 내 강의 추가 후 OK 상태 반환
        } catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
//            return ResponseEntity.badRequest().body(studentAndCustomResult);
        }
    }
    /**
     * 해당 년도/학기/시간표 내 커스텀 강의 삭제 (커스텀 O ) -> 쿼리 확인
     * @param lectureAndTimeSlotResult
     * @param year
     * @param semester
     * @param tableName
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/deleteLecture/{tableName}")
    public ResponseEntity<?> deleteLecture(
            @AuthenticationPrincipal Long id,
            @RequestBody LectureAndTimeSlotResult<LectureDto,List<TimeSlotDto>> lectureAndTimeSlotResult,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "tableName") String tableName
    ){
        try {

            Student student=studentRepository.findById(id);
            Lecture lecture = lectureRepository.findByLectureNumAndYearAndSemester(lectureAndTimeSlotResult.lectureDto.getId(),lectureAndTimeSlotResult.lectureDto.getYearOfLecture(),lectureAndTimeSlotResult.lectureDto.getSemester());

            timeTableService.deleteLecture(student.getNumber(),year,semester,tableName,lecture);

            return new ResponseEntity<>(HttpStatus.OK); //시간표 내 강의 추가 후 OK 상태 반환
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 해당 년도/학기/시간표 내 커스텀 강의 정보 수정
     * @param lectureAndTimeSlotResult
     * @param year
     * @param semester
     * @param tableName
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/updateCustomLecture/{tableName}")
    public ResponseEntity<?> updateCustomLecture(
            @RequestBody LectureAndTimeSlotResult<LectureDto,List<TimeSlotDto>> lectureAndTimeSlotResult,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "tableName") String tableName
    ){
        try {
            Lecture lecture = lectureRepository.findByLectureNumAndYearAndSemester(lectureAndTimeSlotResult.lectureDto.getId(),lectureAndTimeSlotResult.lectureDto.getYearOfLecture(),lectureAndTimeSlotResult.lectureDto.getSemester());

            List<TimeSlot> timeSlots = lectureAndTimeSlotResult.timeSlotDtoList.stream().map((timeSlotDto)-> {
                return timeSlotRepository.findByTimeSlot(timeSlotDto.getDay(), timeSlotDto.getStartTime(), timeSlotDto.getEndTime())
                        .orElseGet(() -> {
                            return TimeSlot.from(timeSlotDto).orElseThrow(()->new IllegalStateException("error"));
                        });
            }).collect(Collectors.toList());

            lectureService.updateLectureInfo(lecture.getLectureNumber(),year,semester,lectureAndTimeSlotResult.lectureDto.getLectureName(),timeSlots);

            return new ResponseEntity<>(HttpStatus.OK); //시간표 내 강의 추가 후 OK 상태 반환
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 특정 년도/학기의 studentlecture 내의 강의 학점(Gpa) 입력
     * @param
     * @param year
     * @param semester
     * @param lectureNum
     * @return
     */
    @PostMapping("/updateGpa/{lectureNum}/{Gpa}")
    public ResponseEntity<?> updateGpa(
            @AuthenticationPrincipal Long id,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "lectureNum") String lectureNum,
            @PathVariable(value =  "Gpa") String Gpa
    ){
        try {
            Student student = studentRepository.findById(id);
            //Student student = studentRepository.findByNumber(studentDTO.getNumber());
            lectureService.updateGpa(student.getNumber(), year, semester, lectureNum, Gpa);

            return new ResponseEntity<>(HttpStatus.OK); //Gpa 입력 후 OK 상태 반환
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //======================================LectureDto, TimeTableDto====================================//

    @Data
    @AllArgsConstructor

    static class LectureAndTimeSlotResult<A,B>{
        private A lectureDto; //customLectureDto -> lectureDto 로 변수명 변경(수연)
        private B timeSlotDtoList;
    }

    @Data
    @AllArgsConstructor
    static class LectureResult<T>{
        private T lectureList;
    }

    @Data
    @AllArgsConstructor
    static class TableResult<T>{
        private T totalTableList;
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
    static class TimeTableDto {
        private int id;
        private String tableName;

        private boolean isPrimary;

        //렉처리스트 추가(수연)
        private List<LectureDto> lectureList;

        public TimeTableDto(int id, TimeTable timeTable) {
            this.id = id;
            tableName=timeTable.getTableName();
            isPrimary= timeTable.isPrimary();
            lectureList=timeTable.getLectures().stream().map(timeTableLecture -> new LectureDto(timeTableLecture.getLecture())).collect(Collectors.toList());
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
        private boolean isDup;

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
            isDup=false;
        }
    }
}
