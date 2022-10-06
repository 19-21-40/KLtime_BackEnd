package com.example.demo.Service;

import com.example.demo.Repository.*;
import com.example.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
     * 시간표 추가 (기본시간표, 추가시간표 포함 => test 코드에서 기본시간표 만들기 위해서 만듦)
     */
    @Transactional
    public Long addTimeTable(Long studentId, int grade, int semester, boolean isPrimary, String tableName, Long lectureId){

        //엔티티 조회
        Student student = studentRepository.findById(studentId);
        Lecture lecture = lectureRepository.findOne(lectureId);

        //시간표강의 생성
        TimeTableLecture timeTableLecture = TimeTableLecture.createTimeTableLecture(lecture);

        //studentlecture 생성 (기본 시간표)
        if(isPrimary==true){
            //GPA == null 을 일단 default로 함
            StudentLecture studentLecture = StudentLecture.createStudentLecture(student,lecture,null, grade);
        }

        //시간표 생성
        TimeTable timeTable = TimeTable.createTimetable(student, tableName,grade, semester, isPrimary, timeTableLecture);

        //시간표 저장
        timeTableRepository.save(timeTable);
        return timeTable.getId();
    }

    /**
     * 오버로딩
     * 시간표 추가 (기본시간표 X => studentlectures 엔 추가 안됨)
     */
    @Transactional
    public Long addTimeTable(Long studentId, int grade, int semester,String tableName, Long lectureId){
        return addTimeTable(studentId,grade, semester,false,tableName,lectureId);
    }

    /**
     * 시간표 삭제 (메인시간표 X => studentlectures 는 건들지 않음)
     */
    @Transactional
    public void deleteTimeTable(Long studentId, Long timetableId){
        //시간표 엔티티 조회
        Student student = studentRepository.findById(studentId);
        TimeTable timeTable = timeTableRepository.findOne(timetableId);

        //시간표 삭제 (cascade 때문에 timetablelectures도 같이 삭제됨)
        if(timeTable.isPrimary()==false){
            timeTableRepository.delete(timeTable);
        }
//        student.delete(timeTable);
    }

    /**
     * 기본 시간표로 변경 (studentlectures 바뀜 => 기존 기본시간표에 있는 것들 삭제하고, 변경한 기본시간표에 있는 것들을 추가)
     */
    @Transactional
    public void changePrimary(Long studentId, Long timetableId) {
        //엔티티 조회
        Student student = studentRepository.findById(studentId);
        TimeTable newPrimaryTimeTable = timeTableRepository.findOne(timetableId);
        validateDuplicatePrimary(newPrimaryTimeTable, student, newPrimaryTimeTable.getSemester()); //기본시간표 중복 체크
        List<StudentLecture> studentLectures= studentLectureRepository.findByStudentAndGradeAndSemester(student, newPrimaryTimeTable.getGrade(), newPrimaryTimeTable.getSemester());

        //기존 기본시간표의 studentlecture 들을 삭제
        for(StudentLecture lecture : studentLectures){
            studentLectureRepository.delete(lecture);
        }

        //기본 시간표 변경
        TimeTable oldPrimaryTimeTable= timeTableRepository.findByStudentAndGradeAndSemesterAndPrimary(student, newPrimaryTimeTable.getGrade(), newPrimaryTimeTable.getSemester(), true);
        oldPrimaryTimeTable.setPrimary(false);
        newPrimaryTimeTable.setPrimary(true);

        //변경한 기본시간표에 있는 강의들로 studentlecture 들 생성 (추가)
        List<TimeTableLecture> timeTableLectures = newPrimaryTimeTable.getLectures();
        for(int i=0;i<timeTableLectures.size();i++){
            Lecture lecture = timeTableLectures.get(i).getLecture();
            //GPA == null 을 일단 default 로 함
            StudentLecture studentLecture = StudentLecture.createStudentLecture(student,lecture,null, newPrimaryTimeTable.getGrade()); //엔티티 조회
            student.addStudentLecture(studentLecture); //studentlecture 생성(추가)
        }
    }

    //기본시간표 중복 체크
    private void validateDuplicatePrimary(TimeTable timeTable, Student student, int semester) {
        List<TimeTable> findPrimarys = timeTableRepository.findDupliPrimary(student, student.getGrade(),semester,true);
        if (!findPrimarys.isEmpty()) {
            throw new IllegalStateException("기본 시간표가 이미 존재합니다.");
        }
    }

    /**
     * 시간표 이름 변경
     */
    @Transactional
    public void changeTimeTableName(Long studentId, Long timetableId, String tableName) {
        //엔티티 조회
        Student student = studentRepository.findById(studentId);
        TimeTable timeTable = timeTableRepository.findOne(timetableId);

        //시간표 이름 변경
        timeTable.setTableName(tableName);
    }

    //======================================(LectureService->TimeTableService)====================================//

    /**
     * 시간표에서 강의(커스텀 X) 한개 추가
     * 기본시간표에서 강의 추가하면 studentlecture 생성(추가)
     */
    @Transactional
    public void addLecture(Long studentId, Long timetableId, Long lectureId) {
        //엔티티 조회
        Student student = studentRepository.findById(studentId);
        TimeTable timeTable = timeTableRepository.findOne(timetableId);
        Lecture lecture = lectureRepository.findOne(lectureId);
        TimeTableLecture timeTableLecture = TimeTableLecture.createTimeTableLecture(lecture);

        //시간표강의 생성(강의 추가)
        timeTable.addTimeTableLecture(timeTableLecture);

        //기본시간표라면, studentlecture 생성(추가)
        if(timeTable.isPrimary()==true){
            //GPA == null 을 일단 default로 함
            StudentLecture studentLecture = StudentLecture.createStudentLecture(student,lecture,null, timeTable.getGrade()); //엔티티 조회
            student.addStudentLecture(studentLecture); //studentlecture 생성(추갸)
        }
    }

    /**
     * 시간표에서 커스텀강의 한개 추가
     * 기본시간표에서 강의 추가하면 studentlecture 생성(추가)
     * 오버로딩
     */
    @Transactional
    public void addCustomLecture(Long studentId, Long timetableId,
                                 String name,
                                 String professor,
                                 String section,
                                 String sectionDetail,
                                 int credit,
                                 int level,
                                 String departmentName,
                                 int yearOfLecture,
                                 int semester) {
        //커스텀 강의 추가(생성)
        Long customLectureId = lectureService.addCustom(name,professor,section,sectionDetail,credit,level,departmentName,yearOfLecture,semester);

        addLecture(studentId,timetableId,customLectureId); //오버로딩
    }

    /**
     * 시간표에서 강의(커스텀 포함) 삭제
     * 기본시간표에서 강의 삭제하면 studentlecture 삭제됨
     */
    //timeTableLectureRepository 에서 delete 쿼리를 날려주면 굳이 timetableId 인자를 쓸 필요가 없어짐.. 레포지토리에 추가해야함
    @Transactional
    public void deleteLecture(Long studentId, Long timetableId, TimeTableLecture timetableLecture) {
        //엔티티 조회
        TimeTable timeTable = timeTableRepository.findOne(timetableId);

        //커스텀 강의라면, lecture 삭제
        if(timetableLecture.getLecture().isCustom()==true){
            Lecture customLecture=timetableLecture.getLecture();
            lectureRepository.delete(customLecture);
        }

        //시간표에서 강의 삭제(timeTableLecture 삭제)
        timeTableLectureRepository.delete(timetableLecture);

        //기본시간표라면, studentlecture 삭제
        if(timeTable.isPrimary()==true){
            //엔티티 조회
            Student student = studentRepository.findById(studentId);
            StudentLecture studentLecture = studentLectureRepository.findById(timetableLecture.getLecture().getId());
            studentLectureRepository.delete(studentLecture);
        }
    }

}
