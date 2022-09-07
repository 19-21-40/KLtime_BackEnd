package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "time_slot")
public class TimeSlot {

    @Id
    @GeneratedValue
    @Column(name = "time_slot_id")
    private Long id;

    private String dayName;
    private String startTime;
    private String endTime;


    public static TimeSlot createTimeSlot(String dayName, String startTime, String endTime){
        TimeSlot timeSlot=new TimeSlot();
        timeSlot.setDayName(dayName);
        timeSlot.setStartTime(startTime);
        timeSlot.setEndTime(endTime);


        return timeSlot;
    }

}
