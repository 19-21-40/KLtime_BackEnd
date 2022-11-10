package com.example.demo.Service;

import com.example.demo.Repository.*;
import com.example.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


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
    private final TimeSlotRepository timeSlotRepository;
    
    //현재 해당 년도/학기 불러오는 로직으로 바꿔야 함
    private Integer currentYear= LocalDateTime.now().getYear();
    private String currentSemester= "2학기"; //수정해야함.. 어케하지


//    /**
//     * 회원 가입 후 기본시간표 자동 생성
//     */
//    @Transactional
//    public void addDefaultTimeTable(String number){
//        Student student = studentRepository.findByNumber(number);
//        //int currentYear=2022; // 수정해야함...
//        for(int i= student.getAdmissionYear();i<= currentYear;i++) {
//            for (int j = 1; j <= 2; j++) { //4로 고치면 String 으로 semester 바꿔야함
//                //시간표 생성
//                String semester = j + "학기";
//                String defaultTableName = createDefaultTableName(student, i, semester);
//                TimeTable timeTable = TimeTable.createTimetable(student, defaultTableName, i, semester, true);
//            }
//        }
//    }
//
//    /**
//     * 시간표 기본 이름 자동 생성
//     */
//    @Transactional
//    public String createDefaultTableName(Student student, int yearOfTimetable, String semester){
//        //엔티티 조회
//        //Student student = studentRepository.findByNumber(number);
//        System.out.println("학번:" + student.getNumber() + "/년도:" + yearOfTimetable );
//        List<TimeTable> tableList = timeTableRepository.findByStudentAndYearAndSemesterWithLecture(student,yearOfTimetable,semester);
//
//        //시간표 이름 생성
//        int tableNumber = tableList.size() + 1;
//
//        String tableName= "시간표" + tableNumber;
//        for (TimeTable timeTable : tableList) {
//            if (Objects.equals(timeTable.getTableName(), tableName)){
//                tableName = "시간표" + (tableNumber+1);
//            }
//        }
//        return tableName;
//    }
    /**
      * 회원 가입 후 기본시간표 자동 생성
     */
    @Transactional
    public void addDefaultTimeTable(String number){
        Student student = studentRepository.findByNumber(number);
        int currentYear=LocalDateTime.now().getYear();
        String[] semesterList={"1학기","2학기","계절학기(하계)","계절학기(동계)"};
        for(int year= student.getAdmissionYear();year<= currentYear;year++) {
            for (String semester:semesterList) {
                //시간표 생성
                String defaultTableName = createDefaultTableName(student, year, semester);
                TimeTable timeTable = TimeTable.createTimetable(student, defaultTableName, year, semester, true);
            }
        }
    }

    /**
     * 시간표 이름 자동 생성
     */
    @Transactional
    public String createDefaultTableName(Student student, int yearOfTimetable, String semester){
        //엔티티 조회
        //Student student = studentRepository.findByNumber(number);
        System.out.println("학번:" + student.getNumber() + "/년도:" + yearOfTimetable );
        List<TimeTable> tableList = timeTableRepository.findByStudentAndYearAndSemesterWithLecture(student,yearOfTimetable,semester);

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
     *
     * @param student
     * @param yearOfTimetable
     * @param semester
     */
    @Transactional
    public Long addTimeTable(Student student, int yearOfTimetable, String semester) {
        String defaultTableName = createDefaultTableName(student,yearOfTimetable,semester);

        return addTimeTable(student, defaultTableName, yearOfTimetable, semester);
    }

    /**
     * 시간표 추가 (기본시간표 X => studentlectures 엔 추가 안됨)
     */
    @Transactional
    public Long addTimeTable(Student student, String name, int yearOfTimetable, String semester){

        //시간표 생성
        TimeTable timeTable = TimeTable.createTimetable(student, name, yearOfTimetable, semester, false);

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

        //시간표 삭제 (cascade 때문에 timetablelecture  s도 같이 삭제됨)
        if(!timeTable.isPrimary()){ //기본 시간표 아닐 때(경고 문구 날려줘야 하나?)
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


        //해당 년도/학기를 제외한 기존 기본시간표의 student lecture 들을 삭제
        if(yearOfTimetable != currentYear || !semester.equals(currentSemester)){
            for(StudentLecture studentLecture : studentLectures){
                studentLectureRepository.delete(studentLecture);
            }
        }

        //기본 시간표 변경
        TimeTable oldPrimaryTimeTable= timeTableRepository.findByStudentAndYearAndSemesterAndPrimary(student, yearOfTimetable, semester, true);
        oldPrimaryTimeTable.setPrimary(false);
        newPrimaryTimeTable.setPrimary(true);

        if(yearOfTimetable != currentYear || !semester.equals(currentSemester)){ //해당 년도/학기 제외
            List<TimeTableLecture> timeTableLectures = newPrimaryTimeTable.getLectures();
            for(int i=0;i<timeTableLectures.size();i++){
                Lecture lecture = timeTableLectures.get(i).getLecture();
                //GPA == null 을 일단 default 로 함
                StudentLecture studentLecture = StudentLecture.createStudentLecture(student,lecture,null); //엔티티 조회
                student.addStudentLecture(studentLecture); //studentlecture 생성(추가)
            }
        }


    }

    @Transactional
    public void changePrimary(Student student, TimeTable newTimeTable,TimeTable oldTimeTable) {
        //엔티티 조회
        //validateDuplicatePrimary(newPrimaryTimeTable, student, newPrimaryTimeTable.getSemester()); //기본시간표 중복 체크
        List<StudentLecture> studentLectures= studentLectureRepository.findByStudentAndTimeTable(student,newTimeTable);

        //기존 기본시간표의 student lecture 들을 삭제
        for(StudentLecture lecture : studentLectures){
            studentLectureRepository.delete(lecture);
        }

        //기본 시간표 변경
        TimeTable oldPrimaryTimeTable= timeTableRepository.findByStudentAndYearAndSemesterAndPrimary(student, newTimeTable.getYearOfTimetable(), newTimeTable.getSemester(), true);
        oldPrimaryTimeTable.setPrimary(false);
        newTimeTable.setPrimary(true);


        //변경한 기본시간표에 있는 강의들로 해당 년도/학기를 제외한 studentlecture 들 생성 (추가)
        if(oldTimeTable.getYearOfTimetable() != currentYear || !oldTimeTable.getSemester().equals(currentSemester)){ //해당 년도/학기 제외
            List<TimeTableLecture> timeTableLectures = newTimeTable.getLectures();
            for (TimeTableLecture timeTableLecture : timeTableLectures) {
                Lecture lecture = timeTableLecture.getLecture();
                StudentLecture studentLecture = StudentLecture.createStudentLecture(student, lecture, null); //엔티티 조회
                student.addStudentLecture(studentLecture); //studentlecture 생성(추가)
            }
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
        List<TimeTable> findTableNames=timeTableRepository.findDupliTableName(student,yearOfTimetable, semester,tableName);
        if (!findTableNames.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 이름입니다.");
        }
    }

    /**
     * 시간표 이름 변경
     */
    @Transactional
    public void changeTimeTableName(Student student, int yearOfTimetable, String semester,String oldTableName,String newTableName) {
        //엔티티 조회
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
     *
     * @param number
     * @param yearOfTimeTable
     * @param semester
     * @param tableName
     * @param lecture
     */

    @Transactional
    public void addLecture(String number, int yearOfTimeTable, String semester,String tableName, Lecture lecture,String gpa) {
        String[] passList={"B0","B+","A0","A+","P"};
        System.out.println("year: "+ yearOfTimeTable + " semester: " + semester);
        //엔티티 조회

        Student student = studentRepository.findByNumber(number);
        //Lecture lecture = lectureRepository.findByLectureNum(lectureNum);
        TimeTable timeTable = timeTableRepository.findByStudentAndYearAndSemesterAndName(student,yearOfTimeTable,semester,tableName);


        //외국인만 수강 가능이면 강의 추가가 안되야 함
        if(lecture.getNotes()==null || !lecture.getNotes().contains("외국인")){
            //studentLecture 을 조회해서 해당 강의 이름이 존재하는지 파악하고, 학점이 P 이거나 B0 이상이면 강의 추가가 안되야 함
            Optional<StudentLecture> retakeLecture =studentLectureRepository.findByStudentAndLectureName(student,lecture.getName());
            if(retakeLecture.isEmpty()||!Arrays.stream(passList).anyMatch(Gpa->Gpa.equals(retakeLecture.get().getGpa())) ){
                //시간표강의 생성(강의 추가)
                TimeTableLecture timeTableLecture = TimeTableLecture.createTimeTableLecture(lecture);
                timeTable.addTimeTableLecture(timeTableLecture);

                //기본시간표라면, studentlecture 생성(추가)
                if(timeTable.isPrimary()){
                    if(yearOfTimeTable != currentYear || !semester.equals(currentSemester)){ //해당 년도/학기 제외
                        //GPA == null 을 일단 default로 함
                        StudentLecture studentLecture = StudentLecture.createStudentLecture(student,lecture,gpa); //엔티티 조회
                        student.addStudentLecture(studentLecture); //studentlecture 생성(추갸)
                    }
                }
            }
        }
    }


    @Transactional
    public void addLecture(String number, int yearOfTimeTable, String semester,String tableName, Lecture lecture) {
        addLecture(number, yearOfTimeTable, semester, tableName, lecture,null);
    }

    /**
     * 시간표에서 커스텀강의 한개 추가
     * 기본시간표에서 강의 추가하면 studentlecture 생성(추가)
     * 여기서의 lecture는 lectureDto -> lecuture
     * 오버로딩
     */
    @Transactional
    public void addCustomLecture(String number, int yearOfTimetable, String semester,String tableName, Lecture lecture, List<TimeSlot> timeSlots,String gpa)
    {
        //커스텀 강의 추가(생성)
        Long customLectureId = lectureService.addCustom(lecture,timeSlots);
        Lecture customLecture = lectureRepository.findOne(customLectureId);

        addLecture(number,yearOfTimetable,semester,tableName,customLecture,gpa); //오버로딩
    }

    @Transactional
    public void addCustomLecture(String number, int yearOfTimetable, String semester,String tableName, Lecture lecture, List<TimeSlot> timeSlots) {
        addCustomLecture(number, yearOfTimetable, semester, tableName, lecture, timeSlots,null);
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
        TimeTableLecture timeTableLecture = timeTableLectureRepository.findByTimetableAndLecture(timeTable,lecture).get();

        //커스텀 강의라면, lecture와 해당 timeslot 삭제
        if(lecture.isCustom()){
            //lecture 삭제
            lectureRepository.delete(lecture);
        }

        //시간표에서 강의 삭제(timeTableLecture 삭제)
        timeTableLectureRepository.delete(timeTableLecture);

        //기본시간표라면, studentlecture 삭제
        if(timeTable.isPrimary()){
            if(yearOfTimetable != currentYear || !semester.equals(currentSemester)){
                //엔티티 조회
                StudentLecture studentLecture = studentLectureRepository.findByStudentAndLecture(timeTableLecture.getLecture(),student);
                studentLectureRepository.delete(studentLecture);
            }
        }
    }

}
