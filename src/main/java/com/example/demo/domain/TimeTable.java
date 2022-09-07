package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    //cascade 추가 (transientpropertyvalueexception)
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "student_id")
    private Student student;
    private String tableName;
    private int grade;
    private int semester;
    private boolean isPrimary;

    //==연관관계 메서드==//
    public void setStudent(Student student) {
        this.student = student;
        student.getTimetables().add(this);
    }
    public void addTimeTableLecture(TimeTableLecture timeTableLecture) {
        lectures.add(timeTableLecture);
        timeTableLecture.setTimeTable(this);
    }

    //==생성 메서드==//
    public static TimeTable createTimetable( Student student, String tableName, int grade, int semester, boolean isPrimary, TimeTableLecture... lectures) {
        TimeTable timeTable = new TimeTable();
        timeTable.setStudent(student);
        timeTable.setTableName(tableName);
        timeTable.setGrade(grade);
        timeTable.setSemester(semester);
        timeTable.setPrimary(isPrimary);
        for (TimeTableLecture timeTableLecture : lectures) {
            timeTable.addTimeTableLecture(timeTableLecture);
        }
        return timeTable;
    }
}
