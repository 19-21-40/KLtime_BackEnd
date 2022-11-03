package com.example.demo.domain;

import com.example.demo.controller.TimeTableController;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Optional;

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

    //TimeSlotDto->TimeSlot 바꾸는 함수
    public static Optional<TimeSlot> from(TimeTableController.TimeSlotDto timeSlotDto){
        TimeSlot timeSlot = createTimeSlot(
                timeSlotDto.getDay(),
                timeSlotDto.getStartTime(),
                timeSlotDto.getEndTime()
        );
        return Optional.ofNullable(timeSlot);
    }

}
