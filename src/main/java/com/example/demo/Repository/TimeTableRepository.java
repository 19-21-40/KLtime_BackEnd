package com.example.demo.Repository;

import com.example.demo.domain.Department;
import com.example.demo.domain.Student;
import com.example.demo.domain.TimeTable;
import com.example.demo.domain.TimeTableLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
//@Transactional
@RequiredArgsConstructor
public class TimeTableRepository {

//    @PersistenceContext
    private final EntityManager em;

    /**
     * 시간표 저장 (시간표는 중복 저장 되지?)
     */
    public void save(TimeTable timeTable) {
        em.persist(timeTable);
//        return timeTable.getId(); //필요 없음
    }

    /**
     * 시간표 조회?
     */
    public TimeTable findOne(Long id) {
        return em.find(TimeTable.class, id);
    }

    /**
     * 시간표 전체 조회?
     */
//    public List<TimeTable> findAll(){
//        return em.createQuery("select t from TimeTable d",TimeTable.class)
//                .getResultList();
//    }

    /**
     * 학생 별시간표 조회
     */
    public List<TimeTable> findByStudent(Student student){
        return em.createQuery("select t from TimeTable t where t.student = :student", TimeTable.class)
                .setParameter("student", student)
                .getResultList();
    }

    /**
     * 학생과 학년 별 시간표 조회(연도로 수정해야함)
     */
    public List<TimeTable> findByStudentAndYear(Student student, Integer yearOfTimeTable){
        return em.createQuery("select t from TimeTable t where t.student = :student and t.yearOfTimetable =: yearOfTimeTable", TimeTable.class)
                .setParameter("student", student) // 아니 이거 진짜 grade 말고 year 만바꿨는데 이러니ㅜㅜ 아효....
                .setParameter("yearOfTimeTable", yearOfTimeTable)
                .getResultList();
    }

    /**
     * 학생, 년도, 학기 별 시간표 조회
     */
    //페치조인으로 수정해야 함(쿼리 한번만 나가도록)
    public List<TimeTable> findByStudentAndYearAndSemester(Student student, int yearOfTimetable, String semester) {
        return em.createQuery("select t from TimeTable t where t.student =:student and t.yearOfTimetable =: yearOfTimetable and t.semester=:semester", TimeTable.class)
                .setParameter("student", student)
                .setParameter("yearOfTimetable", yearOfTimetable)
                .setParameter("semester", semester)
                .getResultList();
    }


    /**
     * 학생, 년도, 학기, 이름 별 시간표 조회
     */
    //페치조인으로 수정해야 함(쿼리 한번만 나가도록)
    public TimeTable findByStudentAndYearAndSemesterAndName(Student student, int yearOfTimetable, String semester, String tableName) {
        return em.createQuery("select t from TimeTable t where t.student =:student and t.yearOfTimetable =: yearOfTimetable and t.semester=:semester and t.tableName=:tableName", TimeTable.class)
                .setParameter("student", student)
                .setParameter("yearOfTimetable", yearOfTimetable)
                .setParameter("semester", semester)
                .setParameter("tableName", tableName)
                .getSingleResult();
    }


    /**
     * 학생의 년도, 학기 별 기본 시간표 조회
     */
    public TimeTable findByStudentAndYearAndSemesterAndPrimary(Student student, int yearOfTimetable, String semester, boolean isPrimary) {
        return em.createQuery("select t from TimeTable t where t.student =:student and t.yearOfTimetable =: yearOfTimetable and t.semester=:semester and t.isPrimary=:isPrimary", TimeTable.class)
                .setParameter("student", student)
                .setParameter("yearOfTimetable", yearOfTimetable)
                .setParameter("semester", semester)
                .setParameter("isPrimary", isPrimary)
                .getSingleResult();
    }

    //기본 시간표 중복 때문에 추가(수연)
    public List<TimeTable> findDupliPrimary(Student student, int yearOfTimetable, String semester, boolean isPrimary){
        return em.createQuery("select t from TimeTable t where t.student =:student and t.yearOfTimetable =: yearOfTimetable and t.semester=:semester and t.isPrimary=:isPrimary", TimeTable.class)
                .setParameter("student", student)
                .setParameter("yearOfTimetable", yearOfTimetable)
                .setParameter("semester", semester)
                .setParameter("isPrimary", isPrimary)
                .getResultList();
    }

    //시간표 이름 중복 때문에 추가(수연)
    public List<TimeTable> findDupliTableName(Student student, int yearOfTimetable, String semester, boolean isPrimary,String tableName){
        return em.createQuery("select t from TimeTable t where t.student =:student and t.yearOfTimetable =: yearOfTimetable and t.semester=:semester and t.tableName=:tableName", TimeTable.class)
                .setParameter("student", student)
                .setParameter("yearOfTimetable", yearOfTimetable)
                .setParameter("semester", semester)
                .setParameter("tableName", tableName)
                .getResultList();
    }

    /**
     * 시간표 삭제
     */
    public void delete(TimeTable timeTable) {
        em.remove(timeTable);
    }

}
