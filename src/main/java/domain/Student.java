package domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter @Setter
@Entity
public class Student {

    @Id @GeneratedValue
    @Column(name = "student_id")
    private Long Id;
    private String name;
    private String email;

    @OneToMany(mappedBy = "student")
    private List<StudentLecture> myLectures;


    /** 처리해야할것 **/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multi_dept_id")
    private Department multiDept;
    private int totalCredit;
    private int grade;
    private String multiMajor;
}
