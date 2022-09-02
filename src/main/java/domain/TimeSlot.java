package domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class TimeSlot {

    @Id
    @GeneratedValue
    @Column(name = "timeslot_id")
    private Long id;
    private String startTime;
    private String endTime;
    private String day;
}
