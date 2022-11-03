package com.example.demo.controller;

import com.example.demo.Repository.*;
import com.example.demo.Service.TimeTableService;
import com.example.demo.domain.*;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * 시간표 페이지 처음 접속 시 & 년도/학기 변경(선택)시 (해당 년도/학기의 기본시간표가 떠야 함) -> 쿼리 확인
     * @param studentDTO
     * @param year
     * @param semester
     * @return ResponseEntity.ok().body(new TimeTableController.TableResult(timeTableList));
     */
    @PostMapping("/totalTimeTableList")
    public ResponseEntity<?> totalTimeTableList(
            @RequestBody StudentDTO studentDTO,
            //@PathVariable String number,
            @PathVariable(value = "year", required = false) int year,
            @PathVariable(value = "semester", required = false) String semester
    ){
        try {
            if (studentDTO.getToken() != null) {
                Student student = studentRepository.findByNumber(studentDTO.getNumber());
                List<TimeTable> timeTables = timeTableRepository.findByStudentAndYearAndSemesterWithLecture(student,year,semester); //fetch outer join
//                List<TimeTable> timeTables = timeTableRepository.findByStudentAndYearAndSemester(student,year,semester);

                List <TimeTableDto> timeTableList=timeTables.stream()
                        .map(TimeTableDto::new)
                        .collect(Collectors.toList());

//                TimeTable PrimaryTimeTable = timeTableRepository.findByStudentAndYearAndSemesterAndPrimary(student,year,semester,true);
//                List<MyLectureDto> lectureLists = PrimaryTimeTable.getLectures().stream()
//                        .map(timeTableLecture -> new MyLectureDto(timeTableLecture.getLecture()))
//                        .collect(Collectors.toList());

                //return ResponseEntity.ok().body(new TimeTableController.tablesAndLectureResult(timeTableList,lectureLists));
                return ResponseEntity.ok().body(new TableResult(timeTableList));

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
     * 해당 년도/학기 시간표 삭제 -> 쿼리 확인
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
                //Student student = studentRepository.findByNumber(studentDTO.getNumber());
                timeTableService.deleteTimeTable(studentDTO.getNumber(),year,semester,tableName);

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
     * 해당 년도/학기 강의 리스트 -> 쿼리 확인
     * @param studentDTO
     * @param year
     * @param semester
     * @return tablesAndLectureResult(timeTableList,lectureLists)
     */
    @PostMapping("/totalLectureList")
    public ResponseEntity<?> totalLectureList(
            @RequestBody StudentDTO studentDTO,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester
    ){
        try {
            if (studentDTO.getToken() != null) {
                //Student student = studentRepository.findByNumber(studentDTO.getNumber());
                List<Lecture> lectures = lectureRepository.findByYearAndSemesterWithTimeslot(year,semester);
                
                List<LectureDto> searchLectureLists = lectures.stream()
                        .map(lecture -> new LectureDto(lecture))
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
     *
     * @param studentAndCustomResult
     * @param year
     * @param semester
     * @param tableName
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/addCustomLecture/{tableName}")
    public ResponseEntity<?> addCustomLecture(
            @RequestBody StudentAndCustomResult<StudentDTO, LectureDto, List<TimeSlotDto>> studentAndCustomResult,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "tableName") String tableName
    ) {
        try {
            if (studentAndCustomResult.studentDto.getToken() != null) {
                //Student student = studentRepository.findByNumber(studentDTO.getNumber());
                List<TimeSlot>timeSlots=studentAndCustomResult.timeSlotDtoList.stream().map((timeSlotDto)-> {
                    return timeSlotRepository.findByTimeSlot(timeSlotDto.getDay(), timeSlotDto.getStartTime(), timeSlotDto.getEndTime())
                            .orElseGet(() -> {
                                return TimeSlot.from(timeSlotDto).orElseThrow(()->new IllegalStateException("error"));
                            });
                }).collect(Collectors.toList());
                Lecture lecture = Lecture.from(studentAndCustomResult.customLectureDto,timeSlots).
                        orElseThrow(() -> {
                            throw new IllegalStateException("지정된 형식과 일치하지 않습니다.");
                        });
                timeTableService.addCustomLecture(studentAndCustomResult.studentDto.getNumber(), year, semester, tableName, lecture, timeSlots);

                return new ResponseEntity<>(HttpStatus.OK); //시간표 내 강의 추가 후 OK 상태 반환
            } else {
                throw new IllegalStateException("토큰이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
//            return ResponseEntity.badRequest().body(studentAndCustomResult);
        }
    }
    /**
     * 해당 년도/학기/시간표 내 커스텀 강의 삭제 (커스텀 O )
     * @param studentAndCustomResult
     * @param year
     * @param semester
     * @param tableName
     * @return new ResponseEntity<>(HttpStatus.OK)
     */
    @PostMapping("/deleteLecture/{tableName}")
    public ResponseEntity<?> deleteLecture(
            @RequestBody StudentAndCustomResult<StudentDTO, LectureDto,List<TimeSlotDto>> studentAndCustomResult,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "tableName") String tableName
    ){
        try {
            if (studentAndCustomResult.studentDto.getToken() != null) {
                //Student student = studentRepository.findByNumber(studentDTO.getNumber());

//                List<TimeSlot> timeSlots = new ArrayList<>();
//                for(int i=0;i<studentAndCustomResult.getTimeSlotDtoList().size();i++){
//                    TimeSlot timeSlot = TimeSlot.from(studentAndCustomResult.getTimeSlotDtoList().get(i)).
//                            orElseThrow(()->{throw new IllegalStateException("지정된 형식과 일치하지 않습니다.");});
//                    timeSlots.add(timeSlot);
//                }
                List<TimeSlot> timeSlots = studentAndCustomResult.timeSlotDtoList.stream().map((timeSlotDto)-> {
                    return timeSlotRepository.findByTimeSlot(timeSlotDto.getDay(), timeSlotDto.getStartTime(), timeSlotDto.getEndTime())
                            .orElseGet(() -> {
                                return TimeSlot.from(timeSlotDto).orElseThrow(()->new IllegalStateException("error"));
                            });
                }).collect(Collectors.toList());

                Lecture lecture;
                if(studentAndCustomResult.customLectureDto.getId()==null){ //학정번호가 c_로 시작되는지로...
                    lecture = lectureRepository.findByYearAndSemesterAndTimeSlotAndCustom(studentAndCustomResult.customLectureDto.getYearOfLecture(),studentAndCustomResult.customLectureDto.getSemester(),true,timeSlots.get(0));
                }
                else{
                    lecture = lectureRepository.findByLectureNum(studentAndCustomResult.customLectureDto.getId());
                }
                timeTableService.deleteLecture(studentAndCustomResult.studentDto.getNumber(),year,semester,tableName,lecture);
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

    //======================================LectureDto, TimeTableDto====================================//

    @Data
    @AllArgsConstructor
    static class StudentAndCustomResult<A,B,C>{
        private A studentDto;
        private B customLectureDto;
        private C timeSlotDtoList;
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
        private String tableName;

        private boolean isPrimary;

        //렉처리스트 추가(수연)

        private List<LectureDto> myLectureList;

        public TimeTableDto(TimeTable timeTable) {
            tableName=timeTable.getTableName();
            isPrimary= timeTable.isPrimary();
            myLectureList=timeTable.getLectures().stream().map(timeTableLecture -> new LectureDto(timeTableLecture.getLecture())).collect(Collectors.toList());
//            myLectureList=timeTable.getLectures();
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
//            lectureTimes =lecture.getTimes().stream().map(lectureTimeSlot -> lectureTimeSlot.getTimeSlot()).collect(Collectors.toList());
        }
    }
}
