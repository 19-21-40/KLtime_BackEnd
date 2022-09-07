package com.example.demo.Repository;

import com.example.demo.domain.Lecture;
import com.example.demo.domain.LectureTimeSlot;
import com.example.demo.domain.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


@Repository
@Transactional
public class LectureRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Lecture lecture) { em.persist(lecture); }

    /*
     * ====================================단일 데이터=================================
     */

    /**
     * id 값으로 강의 찾기
     * @author 부겸
     * @param id 강의 id
     * @return 해당 id의 강의
     */
    public Lecture findOne(Long id) {
        return em.find(Lecture.class, id);
    }


    public List<Lecture> findAll(LectureSearch lectureSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Lecture> cq = cb.createQuery(Lecture.class);
        Root<Lecture> l = cq.from(Lecture.class);
        List<Predicate> criteria = new ArrayList<>();
        //학정번호 검색
        if (lectureSearch.getLectureNumber() != null) {
            Predicate lectureNumber = cb.equal(l.get("lectureNumber"),
                    lectureSearch.getLectureNumber());
            criteria.add(lectureNumber);
        }
        //강의이름 검색(일부도 가능)
        if (StringUtils.hasText(lectureSearch.getName())) {
            Predicate name =
                    cb.like(l.<String>get("name"), "%" +
                            lectureSearch.getName() + "%");
            criteria.add(name);
        }
        //교수명으로 검색(일부도 가능)
        if (StringUtils.hasText(lectureSearch.getProfessor())) {
            Predicate professor =
                    cb.like(l.<String>get("professor"), "%" +
                            lectureSearch.getName() + "%");
            criteria.add(professor);
        }
        //구분으로 검색
        if (lectureSearch.getSection() != null) {
            Predicate section = cb.equal(l.get("section"),
                    lectureSearch.getSection());
            criteria.add(section);
        }
        //세부구분으로 검색
        if (lectureSearch.getSectionDetail() != null) {
            Predicate sectionDetail = cb.equal(l.get("sectionDetail"),
                    lectureSearch.getSectionDetail());
            criteria.add(sectionDetail);
        }
        //난이도 검색
        if (lectureSearch.getLevel() != null) {
            Predicate level = cb.equal(l.get("level"),
                    lectureSearch.getLevel());
            criteria.add(level);
        }
        //학부이름으로 검색
        if (lectureSearch.getDepartmentName() != null) {
            Predicate departmentName = cb.equal(l.get("departmentName"),
                    lectureSearch.getDepartmentName());
            criteria.add(departmentName);
        }
        //년도 검색
        if (lectureSearch.getYearOfLecture()!=null) {
            Predicate yearOfLecture = cb.equal(l.get("yearOfLecture"),
                    lectureSearch.getYearOfLecture());
            criteria.add(yearOfLecture);
        }
        //학기 검색
        if (lectureSearch.getSemester()!=null) {
            Predicate semester = cb.equal(l.get("semester"),
                    lectureSearch.getSemester());
            criteria.add(semester);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Lecture> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }

//    /**
//     * 년도로 강의 목록 찾기
//     * @param year 강의 년도
//     * @return 년도에 따른 강의 리스트
//     */
//    public List<Lecture> findByYear(int year){
//        return em.createQuery("select L from Lecture L"
//        +" where L.yearOfLecture=:year",Lecture.class)
//                .setParameter("year",year)
//                .getResultList();
//    }
//
//    /**
//     * 이름으로 강의 목록 찾기
//     * @param name 강의 이름
//     * @return 같은 이름의 강의 리스트
//     */
//    public List<Lecture> findByLectureName(String name){
//        return em.createQuery("select L from Lecture L"
//        +" where L.name=:lectureName", Lecture.class)
//                .setParameter("lectureName",name)
//                .getResultList();
//    }
//
//    /**
//     * 교수명으로 강의 목록 찾기
//     */
//
//    /**
//     * 구분으로 강의 목록 찾기
//     */
//
//    /**
//     * 구분 세부항목으로 강의 목록 찾기
//     */
//
//    /**
//     * 학점으로 강의 목록 찾기
//     */
//
//    /**
//     * 난이도로 강의 목록 찾기
//     */
//
//    /**
//     * 학부로 강의 목록 찾기
//     */
//
//    /*
//     * ====================================이름=================================
//     */
//
//    /**
//     * 이름과 교수명으로 강의 목록 찾기
//     */
//
//    /**
//     * 이름과 항목으로 강의 목록 찾기
//     */
//
//    /**
//     * 이름과 학점으로 강의 목록 찾기
//     */
//
//    /**
//     * 이름과 난이도로 강의 목록 찾기
//     */
//
//    /**
//     * 이름과 학부로 강의 목록 찾기
//     */
//
//    /**
//     * 이름과 년도로 강의 목록 찾기
//     * @param name 강의 이름
//     * @param year 강의 년도
//     * @return 해당년도의 같은 이름의 강의 리스트
//     */
//    public List<Lecture> findByLectureNameAndYear(String name,int year){
//        return em.createQuery("select L from Lecture L"
//                        +" where L.name=:name and L.yearOfLecture=:year",Lecture.class)
//                .setParameter("name",name)
//                .setParameter("year",year)
//                .getResultList();
//    }
//
//    /*
//     * ====================================년도=================================
//     */
//
//    /**
//     * 년도와
//     */


    public List<Lecture> findByTimeSlot(TimeSlot timeSlot){
        return em.createQuery("select L from Lecture L join LectureTimeSlot LTS"
                        +" where LTS.lecture.id=L.id and LTS.timeSlot.id=:id"
                        ,Lecture.class)
                .setParameter("id",timeSlot.getId())
                .getResultList();
    }



}
