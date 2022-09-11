package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "student_lecture")
public class StudentLecture {

    @Id
    @GeneratedValue
    @Column(name = "student_lecture_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private int gpa;
    private String comment;
    private int takesGrade;
    private int takesSemester;

    public StudentLecture(){

    }

    public StudentLecture(Student student, Lecture lecture, String gpa, int takesGrade, int takesSemester) {
        this.student = student;
        this.lecture = lecture;
        this.gpa = gpa;
        this.takesGrade = takesGrade;
        this.takesSemester = takesSemester;
    }

    @Override /** lecture inner join 주의 */
    public String toString() {
        return  takesGrade + "학년 " + takesSemester + "학기에 들은 " + lecture.getName() + "이며, 학점은 " + gpa + "입니다.";
    }
}
