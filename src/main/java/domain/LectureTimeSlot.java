package domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "lecture_timeslot")
public class LectureTimeSlot {
    @Id
    @GeneratedValue
    @Column(name = "lecture_timeslot_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tiemslot_id")
    private TimeSlot timeslot;
}
