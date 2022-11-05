package com.example.demo.Repository;

import com.example.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Repository
@RequiredArgsConstructor
public class LectureRepository {
    private final EntityManager em;

    public List<Lecture> findByCustom(boolean isCustom){
        return em.createQuery("select l from Lecture l where l.isCustom=:isCustom",Lecture.class)
                .setParameter("isCustom",isCustom)
                .getResultList();
    }

    public Long save(Lecture lecture) {
        em.persist(lecture);
        return lecture.getId();
    }

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


    public List<Lecture> findByYearAndSemesterWithTimeslot(int yearOfLecture, String semester) {
        return em.createQuery("select distinct l from Lecture l join fetch l.times lt join fetch lt.timeSlot t where l.yearOfLecture =:yearOfLecture and l.semester=:semester and l.isCustom = false", Lecture.class)
                .setParameter("yearOfLecture", yearOfLecture)
                .setParameter("semester", semester)
                .getResultList();
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
        if (lectureSearch.getCategory() != null) {
            Predicate category = cb.equal(l.get("category"),
                    lectureSearch.getCategory());
            criteria.add(category);
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
    /**
     * 이름으로 대표 강의 1개 찾기
     * @param name 강의 이름
     * @return 같은 이름의 강의 리스트
     */
    public Lecture findByLectureName(String name){
        return em.createQuery("select L from Lecture L"
        +" where L.name=:lectureName", Lecture.class)
                .setParameter("lectureName",name)
                .getSingleResult();
    }


//
//    /**
//     * 교수명으로 강의 목록 찾기
//     */
//

    /**
     * Section에 대해 Set타입으로 2022년 강의를 조회함 */
    public List<Lecture> findBySectionDetailSet2022(Set<String> sectionDetailSet) {
        return em.createQuery("select l from Lecture l where l.sectionDetail in :sectionDetailSet")
                .setParameter("sectionDetailSet", sectionDetailSet)
                .getResultList();
    }


    /**
     * 구분 2개로 연도구분없이 강의 목록 찾기
     * 이성훈이 만듬
     */

    // 이성훈이 만듬
    public List<Lecture> findByTwoSection(String section1, String section2) {
        return em.createQuery("select l from Lecture l where l.section =:section1 or l.section =:section2", Lecture.class)
                .setParameter("section1", section1)
                .setParameter("section2", section2)
                .getResultList();
    }

    /**
     * 구분 2개로 2022년 강의 목록 찾기
     * 이성훈이 만듬
     */
    public List<Lecture> findByTwoSection2022(String section1, String section2) {
        return em.createQuery("select l from Lecture l where (l.section =:section1 or l.section =:section2) and l.yearOfLecture =2022", Lecture.class)
                .setParameter("section1", section1)
                .setParameter("section2", section2)
                .getResultList();
    }

    /**
     * 학생의 학과와 단과대학으로 연도구분없이 전필 전선 과목 찾기
     * @param studentDept
     * @return
     */
    public List<Lecture> findMainLecturesByTwoSection(Department studentDept) {
        return em.createQuery("select l from Lecture l where (l.section ='전필' or l.section ='전선') " +
                        "and (l.category like :stDeptName or l.category like :stCollegeName)", Lecture.class)
                .setParameter("stCollegeName", '%' + studentDept.getCollegeName() + '%')
                .setParameter("stDeptName", '%' + studentDept.getName() + '%')
                .getResultList();
    }

    /**
     * 커스텀 강의 삭제
     */
    public void delete(Lecture lecture) {
        em.remove(lecture);
        }

     /** 학생의 학과와 단과대학으로 2022년 전필 전선 과목 찾기
     * @param studentDept
     * @return
     */
    public List<Lecture> findMainLecturesByTwoSection2022(Department studentDept) {
        return em.createQuery("select l from Lecture l where (l.section ='전필' or l.section ='전선') " +
                        "and (l.category like :stDeptName or l.category like :stCollegeName) and l.yearOfLecture=2022", Lecture.class)
                .setParameter("stCollegeName", '%' + studentDept.getCollegeName() + '%')
                .setParameter("stDeptName", '%' + studentDept.getName() + '%')
                .getResultList();

    }

    public List<Lecture> findByDepartment2022(Department studentDept) {
        return em.createQuery("select l from Lecture l where (l.category like :stDeptName or l.category like :stCollegeName ) and l.yearOfLecture=2022", Lecture.class)
                .setParameter("stCollegeName", '%' + studentDept.getCollegeName() + '%')
                .setParameter("stDeptName", '%' + studentDept.getName() + '%')
                .getResultList();
    }
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



    public Lecture findByYearAndSemesterAndTimeSlotAndCustom(int year, String semester,boolean isCustom, TimeSlot timeSlot){
        return em.createQuery("select L from Lecture L left join L.times t where L.yearOfLecture=:year and L.semester=:semester and t.timeSlot=:timeSlot and L.isCustom=:isCustom"
                        ,Lecture.class)
                .setParameter("year",year)
                .setParameter("semester",semester)
                .setParameter("timeSlot",timeSlot)
                .setParameter("isCustom", isCustom)
                .getSingleResult();
    }


    public List<Lecture> findByTimeSlot(TimeSlot timeSlot){
        return em.createQuery("select L from Lecture L left join L.times t where t.timeSlot=:timeSlot"
                        ,Lecture.class)
                .setParameter("timeSlot",timeSlot)
                .getResultList();
    }


    public List<Lecture> findBySectionDetail(String sectionDetail) {
        return em.createQuery("select l from Lecture l where l.sectionDetail =:sectionDetail", Lecture.class)
                .setParameter("sectionDetail", sectionDetail)
                .getResultList();
    }

    public List<Lecture> findBySectionDetail2022(String sectionDetail) {
        return em.createQuery("select l from Lecture l where l.sectionDetail =:sectionDetail and l.yearOfLecture=2022", Lecture.class)
                .setParameter("sectionDetail", sectionDetail)
                .getResultList();
    }


    /**
     * 해당 년도/학기 학정번호로 강의 찾기
     * @param lectureNumber
     * @return lecture
     */
    public Lecture findByLectureNumAndYearAndSemester(String lectureNumber, int year, String semester) {
        return em.createQuery("select l from Lecture l where l.lectureNumber=:lectureNumber and l.yearOfLecture=:year and l.semester=:semester",Lecture.class)
                .setParameter("lectureNumber", lectureNumber)
                .setParameter("year", year)
                .setParameter("semester", semester)
                .getSingleResult();
    }
}
