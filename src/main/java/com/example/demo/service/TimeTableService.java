package com.example.demo.Service;

import com.example.demo.Repository.*;
import com.example.demo.controller.TimeTableController;
import com.example.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;
    private final StudentLectureRepository studentLectureRepository;
    private final TimeTableLectureRepository timeTableLectureRepository;
    private final LectureService lectureService; //서비스 계층끼리 함수 공유해도 되나?

    /**
     * 회원 가입 후 기본시간표 자동 생성 (studentlectures 에 추가)
     */
    @Transactional
    public void addDefaultTimeTable(String number){
        Student student = studentRepository.findByNumber(number);
        int currentYear=2022; // 수정해야함...
        for(int i= student.getAdmissionYear();i<= currentYear;i++) {
            for (int j = 1; j <= 2; j++) { //4로 고치면 String 으로 semester 바꿔야함
                //시간표 생성
                String semester = j + "학기";
                String defaultTableName = createDefaultTableName(student, i, semester);
                TimeTable timeTable = TimeTable.createTimetable(student, defaultTableName, i, semester, true);
            }
        }
    }

    /**
     * 시간표 기본 이름 자동 생성
     */
    @Transactional
    public String createDefaultTableName(Student student, int yearOfTimetable, String semester){
        //엔티티 조회
        //Student student = studentRepository.findByNumber(number);
        System.out.println("학번:" + student.getNumber() + "/년도:" + yearOfTimetable );
        List<TimeTable> tableList = timeTableRepository.findByStudentAndYearAndSemester(student,yearOfTimetable,semester);

        //시간표 이름 생성
        int tableNumber = tableList.size() + 1;

        String tableName= "시간표" + tableNumber;
        for (TimeTable timeTable : tableList) {
            if (Objects.equals(timeTable.getTableName(), tableName)){
                tableName = "시간표" + (tableNumber+1);
            }
        }
        return tableName;
    }

    /**
     * 시간표 추가 (기본시간표 X => studentlectures 엔 추가 안됨)
     */
    // find 개수 where 연도 + 학기 + 해당 student 1
    //select count(t) from timetable where
    @Transactional
    public Long addTimeTable(String number, int yearOfTimetable, String semester){
        //엔티티 조회
        Student student = studentRepository.findByNumber(number);

        //시간표 생성
        String defaultTableName = createDefaultTableName(student,yearOfTimetable,semester);
        TimeTable timeTable = TimeTable.createTimetable(student, defaultTableName, yearOfTimetable, semester, false);

        //시간표 저장
        timeTableRepository.save(timeTable);
        return timeTable.getId();
    }

    /**
     * 시간표 삭제 (메인시간표 X => studentlectures 는 건들지 않음)
     */
    @Transactional
    public void deleteTimeTable(String number, int yearOfTimetable, String semester,String tableName){
        //시간표 엔티티 조회
        Student student = studentRepository.findByNumber(number);
        TimeTable timeTable = timeTableRepository.findByStudentAndYearAndSemesterAndName(student,yearOfTimetable,semester,tableName);

        //시간표 삭제 (cascade 때문에 timetablelectures도 같이 삭제됨)
        if(timeTable.isPrimary()==false){ //기본 시간표 아닐 때(경고 문구 날려줘야 하나?)
            timeTableRepository.delete(timeTable);
        }
//        student.delete(timeTable);
    }

    /**
     * 기본 시간표로 변경 (studentlectures 바뀜 => 기존 기본시간표에 있는 것들 삭제하고, 변경한 기본시간표에 있는 것들을 추가)
     */
    @Transactional
    public void changePrimary(Student student, int yearOfTimetable, String semester, String tableName) {
        //엔티티 조회
        //Student student = studentRepository.findByNumber(number);
        TimeTable newPrimaryTimeTable = timeTableRepository.findByStudentAndYearAndSemesterAndName(student,yearOfTimetable,semester,tableName);
        //validateDuplicatePrimary(newPrimaryTimeTable, student, newPrimaryTimeTable.getSemester()); //기본시간표 중복 체크
        List<StudentLecture> studentLectures= studentLectureRepository.findByStudentAndYearAndSemester(student, yearOfTimetable, semester);

        //기존 기본시간표의 student lecture 들을 삭제
        for(StudentLecture lecture : studentLectures){
            studentLectureRepository.delete(lecture);
        }

        //기본 시간표 변경
        TimeTable oldPrimaryTimeTable= timeTableRepository.findByStudentAndYearAndSemesterAndPrimary(student, yearOfTimetable, semester, true);
        oldPrimaryTimeTable.setPrimary(false);
        newPrimaryTimeTable.setPrimary(true);

        //변경한 기본시간표에 있는 강의들로 studentlecture 들 생성 (추가)
        List<TimeTableLecture> timeTableLectures = newPrimaryTimeTable.getLectures();
        for(int i=0;i<timeTableLectures.size();i++){
            Lecture lecture = timeTableLectures.get(i).getLecture();
            //GPA == null 을 일단 default 로 함
            StudentLecture studentLecture = StudentLecture.createStudentLecture(student,lecture,null); //엔티티 조회
            student.addStudentLecture(studentLecture); //studentlecture 생성(추가)
        }
    }

 /*
    //기본시간표 중복 체크
    private void validateDuplicatePrimary(TimeTable timeTable, Student student, String semester) {
        List<TimeTable> findPrimarys = timeTableRepository.findDupliPrimary(student, timeTable.getYearOfTimetable(), semester,true);
        if (!findPrimarys.isEmpty()) {
            throw new IllegalStateException("기본 시간표가 이미 존재합니다.");
        }
    }
 */

    //시간표 이름 중복 체크
    private void validateDuplicateTableName(Student student, int yearOfTimetable, String semester,String tableName ) {
        List<TimeTable> findTableNames=timeTableRepository.findDupliTableName(student,yearOfTimetable, semester,true,tableName);
        if (!findTableNames.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 이름입니다.");
        }
    }

    /**
     * 시간표 이름 변경
     */
    @Transactional
    public void changeTimeTableName(String number, int yearOfTimetable, String semester,String oldTableName,String newTableName) {
        //엔티티 조회
        Student student = studentRepository.findByNumber(number);
        TimeTable timeTable = timeTableRepository.findByStudentAndYearAndSemesterAndName(student,yearOfTimetable,semester,oldTableName);

        //시간표 이름 변경
        validateDuplicateTableName(student,yearOfTimetable,semester,newTableName);
        timeTable.setTableName(newTableName);

        //시간표 저장
        timeTableRepository.save(timeTable);
    }

    //======================================(LectureService->TimeTableService)====================================//

    /**
     * 시간표에서 강의(커스텀 X) 한개 추가
     * 기본시간표에서 강의 추가하면 studentlecture 생성(추가)
     */
    @Transactional
    public void addLecture(String number, int yearOfTimeTable, String semester,String tableName, Lecture lecture) {
        //엔티티 조회
        Student student = studentRepository.findByNumber(number);
        //Lecture lecture = lectureRepository.findByLectureNum(lectureNum);
        TimeTable timeTable = timeTableRepository.findByStudentAndYearAndSemesterAndName(student,yearOfTimeTable,semester,tableName);
        TimeTableLecture timeTableLecture = TimeTableLecture.createTimeTableLecture(lecture);

        //시간표강의 생성(강의 추가)
        timeTable.addTimeTableLecture(timeTableLecture);

        //기본시간표라면, studentlecture 생성(추가)
        if(timeTable.isPrimary()){
            //GPA == null 을 일단 default로 함
            StudentLecture studentLecture = StudentLecture.createStudentLecture(student,lecture,null); //엔티티 조회
            student.addStudentLecture(studentLecture); //studentlecture 생성(추갸)
        }
    }

    /**
     * 시간표에서 커스텀강의 한개 추가
     * 기본시간표에서 강의 추가하면 studentlecture 생성(추가)
     * 오버로딩
     */
    @Transactional
    public void addCustomLecture(String number, int yearOfTimetable, String semester,String tableName, Lecture lecture, List<TimeSlot> timeSlots)
    {
        //커스텀 강의 추가(생성)
        Long customLectureId = lectureService.addCustom(lecture,timeSlots);
        Lecture customLecture = lectureRepository.findOne(customLectureId);

        addLecture(number,yearOfTimetable,semester,tableName,customLecture); //오버로딩
    }

    /**
     * 시간표에서 강의(커스텀 포함) 삭제
     * 기본시간표에서 강의 삭제하면 studentlecture 삭제됨
     */
    //timeTableLectureRepository 에서 delete 쿼리를 날려주면 굳이 timetableId 인자를 쓸 필요가 없어짐.. 레포지토리에 추가해야함
    @Transactional
    public void deleteLecture(String number, int yearOfTimetable, String semester, String tableName, Lecture lecture) {
        //엔티티 조회
        Student student = studentRepository.findByNumber(number);
        TimeTable timeTable = timeTableRepository.findByStudentAndYearAndSemesterAndName(student,yearOfTimetable,semester,tableName);
        TimeTableLecture timeTableLecture = timeTableLectureRepository.findByTimetableAndLecture(timeTable,lecture);

        //커스텀 강의라면, lecture 삭제
        if(timeTableLecture.getLecture().isCustom()){
            Lecture customLecture=timeTableLecture.getLecture();
            lectureRepository.delete(customLecture);
        }

        //시간표에서 강의 삭제(timeTableLecture 삭제)
        timeTableLectureRepository.delete(timeTableLecture);

        //기본시간표라면, studentlecture 삭제
        if(timeTable.isPrimary()){
            //엔티티 조회
            StudentLecture studentLecture = studentLectureRepository.findById(timeTableLecture.getLecture().getId());
            studentLectureRepository.delete(studentLecture);
        }
    }
}
