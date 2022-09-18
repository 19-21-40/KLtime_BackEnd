package com.example.demo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity @Getter @Setter
@Table(name = "timetable_lecture")
@NoArgsConstructor
public class TimeTableLecture {

    @Id @GeneratedValue
    @Column(name = "timetable_lecture_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private TimeTable timeTable;
    
    //==생성 메서드==//
    public static TimeTableLecture createTimeTableLecture(Lecture lecture){
        TimeTableLecture timeTableLecture= new TimeTableLecture();
        timeTableLecture.setLecture(lecture);
        return timeTableLecture;
    }

}
