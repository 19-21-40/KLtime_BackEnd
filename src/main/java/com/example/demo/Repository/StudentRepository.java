package com.example.demo.Repository;

import com.example.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentRepository {

    private final EntityManager em;

    public Student save(Student student) {
        em.persist(student);
        return student;
    }

    public Student findById(Long id) {
        return em.find(Student.class, id);
    }

    public Student findByNumber(String number){
        return em.createQuery("select s from Student s where s.number=:number",Student.class)
                .setParameter("number",number)
                .getSingleResult();
    }

    public Student findByNumberAndPassword(String number,String password) {
        return em.createQuery("select s from Student s where s.number=:number and s.password=:password", Student.class)
                .setParameter("number", number)
                .setParameter("password", password)
                .getSingleResult();
    }


    //중복 학번때문에 추가(수연)

    /**
     * id 중복체크
     * @param id
     * @return
     */
    public List<Student> findDupliOne(Long id){
        return em.createQuery("select s from Student s where s.id = :id", Student.class)
                .setParameter("id", id)
                .getResultList();

    }

    public Student findByStudentNumWithLecture(String number) {
        return em.createQuery("select s from Student s join fetch s.myLectures sl join fetch sl.lecture l where s.number =:number", Student.class)
                .setParameter("number", number)
                .getSingleResult();
    }

    public Student findByStudnetNumWithDepartment(String number) {
        return em.createQuery("select s from Student s join fetch s.department where s.number =:number", Student.class)
                .setParameter("number", number)
                .getSingleResult();
    }


    public List<Student> findByLecture(Lecture lecture) {
        return em.createQuery("select s from Student s left join s.myLectures sl where sl.lecture =:lecture", Student.class)
                .setParameter("lecture", lecture)
                .getResultList();
    }
//
    public List<Student> findByLectureAndDept(Lecture lecture, Department dept) {
        return em.createQuery("select s from Student s left join s.myLectures sl where sl.lecture =:lecture and s.department =:dept", Student.class)
                .setParameter("lecture", lecture)
                .setParameter("dept", dept)
                .getResultList();

    }

    public List<Student> findByLectureAndDeptAndGrade(Lecture lecture, Department dept, int grade) {
        return em.createQuery("select s from Student s left join s.myLectures sl "
                + "where sl.lecture =:lecture and s.department =:dept and s.grade =:grade", Student.class)
                .setParameter("lecture", lecture)
                .setParameter("dept", dept)
                .setParameter("grade", grade)
                .getResultList();

    }

    public Boolean existsByNumber(String number){
        return em.createQuery("select count(s)>0 from Student s where s.number=:number",Boolean.class)
                .setParameter("number",number)
                .getSingleResult();

    }

    public void delete(Student student) {
        em.remove(student);
    }

    //개망
//    public Optional<Student> findBystudentLecture(String lectureName) {
//        try{
//            return Optional.ofNullable(em.createQuery("select s from Student s left join s.myLectures sl left join sl.lecture where sl.lecture.name=:lectureName",Student.class)
//                    .setParameter("lectureName", lectureName)
//                    .getResultList());
//        }catch (NoResultException e){
//            return Optional.empty();
//        }
//
//    }
}
