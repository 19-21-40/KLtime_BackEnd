package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
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

    public Lecture(String lectureNumber, String name) {
        this.lectureNumber = lectureNumber;
        this.name = name;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "preReq_lecture_id")
//    private Lecture preReq;


}
