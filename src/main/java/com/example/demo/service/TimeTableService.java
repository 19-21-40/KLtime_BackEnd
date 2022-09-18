package com.example.demo.Service;

import com.example.demo.Repository.LectureRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.Repository.TimeTableRepository;
import com.example.demo.domain.Lecture;
import com.example.demo.domain.Student;
import com.example.demo.domain.TimeTable;
import com.example.demo.domain.TimeTableLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;

    //시간표에 담자마자 자동저장되는 거라 따로 시간표 저장은 만들지 않음.

    /**
     * 시간표 추가
     */
    @Transactional
    public Long addTimeTable(Long studentId, int semester, boolean isPrimary, String tableName, Long lectureId){

        //엔티티 조회
        Student student = studentRepository.findById(studentId);
        Lecture lecture = lectureRepository.findOne(lectureId);

        //시간표강의 생성
        TimeTableLecture timeTableLecture = TimeTableLecture.createTimeTableLecture(lecture);

        //시간표 생성
        TimeTable timeTable = TimeTable.createTimetable(student, tableName, student.getGrade(), semester, isPrimary, timeTableLecture);

        //시간표 저장
        timeTableRepository.save(timeTable);
        return timeTable.getId();
    }

    /**
     * 시간표 삭제(cascade 초반에 설정했으니 그냥 아무생각없이 삭제함..)
     */
    @Transactional
    public void deleteTimeTable(Long timetableId){
        //시간표 엔티티 조회
        TimeTable timeTable = timeTableRepository.findOne(timetableId);

        //시간표 삭제(참빛에 물어보고 하기)
        timeTable.delete();
    }

    /**
     * 기본 시간표로 변경
     */
    @Transactional
    public void changePrimary(Long studentId, int semester, Long timetableId) {
        //엔티티 조회
        Student student = studentRepository.findById(studentId);
        TimeTable timeTable = timeTableRepository.findOne(timetableId);

        //기본 시간표 변경
        TimeTable primaryTimeTable= timeTableRepository.findByStudentAndGradeAndSemesterAndPrimary(student, student.getGrade(), semester, true);
        primaryTimeTable.setPrimary(false);
        timeTable.setPrimary(true);
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
}
