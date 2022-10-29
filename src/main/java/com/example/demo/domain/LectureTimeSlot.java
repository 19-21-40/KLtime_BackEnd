package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "lecture_time_slot")
public class LectureTimeSlot {
    @Id
    @GeneratedValue
    @Column(name = "lecture_time_slot_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;


    //==연관관계 메서드==
//    public void setLecture(Lecture lecture){
//        this.lecture=lecture;
//        lecture.getTimes().add(this);
//    }

    public void setTimeSlot(TimeSlot timeSlot){
        this.timeSlot=timeSlot;
    }

    //==생성 메서드==

    /**
     * 강의 시간 생성
     * @author 부겸
     * @param timeSlot  TimeSlot 객체
     * @return LectureTimeSlot 객체
     */
    public static LectureTimeSlot createLectureTimeSlot(TimeSlot timeSlot){
        LectureTimeSlot lts=new LectureTimeSlot();
        lts.setTimeSlot(timeSlot);
        return lts;
    }
}
