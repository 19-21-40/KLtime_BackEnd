package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
public class Lecture {

    //semester int->string 변경

    @Id
    @GeneratedValue
    @Column(name = "lecture_id")
    private Long id;

    private String lectureNumber;

    private String name;
    private String professor;
    private String section;
    private String sectionDetail;
    private int credit;
    private int level;
    private String departmentName;
    private int yearOfLecture;
    private int semester; //수정(수연)
    private String notes;

    //추가(수연)
    private boolean isCustom;


    @OneToMany(mappedBy = "lecture")
    private List<LectureTimeSlot> times = new ArrayList<>();

    //이게 과연 어디에 쓰이는가..?
    @OneToMany(mappedBy = "lecture")
    private List<TimeTableLecture> tableListWhichAdd = new ArrayList<>();


    protected Lecture(){

    }
    public Lecture(String lectureNumber, String name) {
        this.lectureNumber = lectureNumber;
        this.name = name;
    }

    // 이성훈이 만듬 ( Timeslot, TimeTable은 생성자에서 배제했음 )
    public Lecture(String lectureNumber, String name, String professor, String section, String sectionDetail, int credit, int level, String departmentName, int yearOfLecture, int semester) {
        this.lectureNumber = lectureNumber;
        this.name = name;
        this.professor = professor;
        this.section = section;
        this.sectionDetail = sectionDetail;
        this.credit = credit;
        this.level = level;
        this.departmentName = departmentName;
        this.yearOfLecture = yearOfLecture;
        this.semester = semester;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "preReq_lecture_id")
//    private Lecture preReq;

    //==ㄹ연관관계 메서드==//


    /**
     * LectureTimeSlot 추가
     * createLectureTimeSlot 을 통해 우선적으로 LectureTimeSlot 객체를 만들어 사용
     * @param lts LectureTimeSlot 객체
     */
    public void addTimes(LectureTimeSlot lts) {
        if(!times.contains(lts)){
            this.times.add(lts);
            lts.setLecture(this);
        }
    }

    public void setTimes(List<LectureTimeSlot> ltsList){
        this.times=ltsList;
        ltsList.forEach(lts->lts.setLecture(this));
    }

    //==생성 메서드==//
    public static Lecture createLecture(
            String lectureNumber,
            String name,
            String professor,
            String section,
            String sectionDetail,
            int credit,
            int level,
            String departmentName,
            int yearOfLecture,
            int semester,
            String notes,
            boolean isCustom){

        Lecture lecture=new Lecture();

        lecture.setLectureNumber(lectureNumber);
        lecture.setName(name);
        lecture.setProfessor(professor);
        lecture.setSection(section);
        lecture.setSectionDetail(sectionDetail);
        lecture.setCredit(credit);
        lecture.setLevel(level);
        lecture.setDepartmentName(departmentName);
        lecture.setYearOfLecture(yearOfLecture);
        lecture.setSemester(semester);
        lecture.setNotes(notes);
        lecture.setCustom(isCustom);

        return lecture;
    }

    //추가(수연)
    //==커스텀강의 생성 메서드==//
    public static Lecture createLecture(
            String name,
            String professor,
            String section,
            String sectionDetail,
            int credit,
            int level,
            String departmentName,
            int yearOfLecture,
            int semester){
        return createLecture(null,name,professor,section,sectionDetail,credit,level,departmentName,yearOfLecture,semester,null,true);
    }
    
    @Override
    public String toString() {
        return "LectureNumber = " + lectureNumber + ", Name = " + name + ", Professor = " + professor;
    }

}
