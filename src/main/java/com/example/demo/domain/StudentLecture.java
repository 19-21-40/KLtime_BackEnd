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

    private String gpa; //수정
    private String comment;
    private int takesGrade;
    private String takesSemester;

    public StudentLecture(){

    }

    //==연관관계 메서드==//


    //==생성 메서드==//
    public static StudentLecture createStudentLecture(Student student, Lecture lecture,String Gpa){
        StudentLecture studentLecture =  new StudentLecture();
        studentLecture.setStudent(student);
        studentLecture.setLecture(lecture);
        studentLecture.setGpa(Gpa);
//        studentLecture.setTakesGrade(grade); //일단 grade 보류(파라미터에서도 삭제)
        studentLecture.setTakesSemester(lecture.getSemester());
        return studentLecture;
    }


    public StudentLecture(Student student, Lecture lecture, String gpa, int takesGrade, String takesSemester) {
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
