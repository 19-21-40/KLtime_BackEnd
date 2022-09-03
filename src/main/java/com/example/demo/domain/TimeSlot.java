package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "timeslot")
public class TimeSlot {

    @Id
    @GeneratedValue
    @Column(name = "timeslot_id")
    private Long id;

    @OneToMany(mappedBy = "timeslot", cascade = CascadeType.ALL)
    private List<LectureTimeSlot> lectureTimeSlots = new ArrayList<>();
    private String startTime;
    private String endTime;
    private String dayName;
}
