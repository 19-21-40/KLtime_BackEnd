package domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @Table(name = "table_st_lecture_id")
public class TableStLecture {

    @Id
    @GeneratedValue
    @Column(name = "table_st_lecture_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentlecture_id")
    private StudentLecture studentLecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private TimeTable timeTable;
}
