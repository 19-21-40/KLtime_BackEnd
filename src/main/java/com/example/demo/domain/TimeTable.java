package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "timetable")
public class TimeTable {

    @Id
    @GeneratedValue
    @Column(name = "timetable_id")
    private Long id;

    @OneToMany(mappedBy = "timeTable", cascade = CascadeType.ALL)
    private List<TimeTableLecture> lectures = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;
    private String tableName;
    private int grade;
    private int semester;
    private boolean isPrimary;
}
