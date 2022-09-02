package domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class TimeTable {

    @Id
    @GeneratedValue
    @Column(name = "timetable_id")
    private Long id;

    @OneToMany(mappedBy = "timeTable")
    private List<TableStLecture> tableStLectures;
    private String tableName;
    private boolean isPrimary;
}
