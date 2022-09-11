package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Student {

    @Id @GeneratedValue
    @Column(name = "student_id")
    private Long id;
    private String name;
    private String email;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentLecture> myLectures = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multi_dept_id")
    private Department multiDept;
    private int totalCredit;
    private int grade;
    private String multiMajor;

    // 이성훈이 9/11에 추가함
    private int admissionYear;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<TimeTable> timetables = new ArrayList<>();

    public Student(String name, Department department, int grade, int admissionYear) {
        this.name = name;
        this.department = department;
        this.grade = grade;
        this.admissionYear = admissionYear;
    }

    public Student() {
    }

    /** 양방향 편의 메서드 */
    public void addLectureToStudent(Lecture lecture, String gpa, int takesGrade, int takesSemester) {
        StudentLecture st = new StudentLecture(this,lecture, gpa, takesGrade, takesSemester);

        this.getMyLectures().add(st);
    }

    public void addCredit(int credit) {
        this.totalCredit += credit;
    }

}
