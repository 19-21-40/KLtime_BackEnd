package com.example.demo.Repository;

import com.example.demo.domain.Department;
import com.example.demo.domain.Student;
import com.example.demo.domain.TimeTable;
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
        return em.createQuery("select d from TimeTable d where d.student = :student", TimeTable.class)
                .setParameter("student", student)
                .getResultList();
    }

    /**
     * 학생과 학년 별 시간표 조회
     */
    public List<TimeTable> findByStudentAndGrade(Student student, int grade){
        return em.createQuery("select t from TimeTable t where t.student = :student and t.grade =: grade", TimeTable.class)
                .setParameter("student", student)
                .setParameter("grade", grade)
                .getResultList();
    }

    /**
     * 학생, 학년, 학기 별 시간표 조회
     */
    public List<TimeTable> findByStudentAndGradeAndSemester(Student student, int grade, int semester) {
        return em.createQuery("select t from TimeTable t where t.student =:student and t.grade =: grade and t.semester=:semester", TimeTable.class)
                .setParameter("student", student)
                .setParameter("grade", grade)
                .setParameter("semester", semester)
                .getResultList();
    }

    /**
     * 학생의 학년, 학기 별 기본 시간표 조회
     */
    public TimeTable findByStudentAndGradeAndSemesterAndPrimary(Student student, int grade, int semester, boolean isPrimary) {
        return em.createQuery("select t from TimeTable t where t.student =:student and t.grade =: grade and t.semester=:semester and t.isPrimary=:isPrimary", TimeTable.class)
                .setParameter("student", student)
                .setParameter("grade", grade)
                .setParameter("semester", semester)
                .setParameter("isPrimary", isPrimary)
                .getSingleResult();
    }

    //기본 시간표 중복 때문에 추가(수연)
    public List<TimeTable> findDupliPrimary(Student student, int grade, int semester, boolean isPrimary){
        return em.createQuery("select t from TimeTable t where t.student =:student and t.grade =: grade and t.semester=:semester and t.isPrimary=:isPrimary", TimeTable.class)
                .setParameter("student", student)
                .setParameter("grade", grade)
                .setParameter("semester", semester)
                .setParameter("isPrimary", isPrimary)
                .getResultList();
    }

}
