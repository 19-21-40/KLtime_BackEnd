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
    private String semester;
    private String notes;


    @OneToMany(mappedBy = "lecture")
    private List<LectureTimeSlot> times = new ArrayList<>();

    @OneToMany(mappedBy = "lecture")
    private List<TimeTableLecture> tableListWhichAdd = new ArrayList<>();


    protected Lecture(){

    }
    public Lecture(String lectureNumber, String name) {
        this.lectureNumber = lectureNumber;
        this.name = name;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "preReq_lecture_id")
//    private Lecture preReq;

    //==연관관계 메서드==


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
            String semester,
            String notes){

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

        return lecture;
    }

    //==비즈니스 로직==//

    /**
     * 시간표 내의 강의 삭제
     */
    public void delete(TimeTableLecture timeTableLecture) {
        tableListWhichAdd.remove(timeTableLecture);
    }

}
