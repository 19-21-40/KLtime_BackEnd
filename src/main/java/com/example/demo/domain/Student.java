package com.example.demo.domain;

import com.example.demo.dto.StudentDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
//import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Getter @Setter
@Entity
@Data
public class Student {

    @Id
    @GeneratedValue
    @Column(name = "student_id")
    private Long id;
    private String number;
    private String name;
    private String password;
    private String email;

    //메인 시간표에 담긴 강의들이 저장됨
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentLecture> myLectures = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multi_dept_id")
    private Department multiDept;

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    @Embedded
    private Credit credit;
    private int grade;
    private String multiMajor;

    // 이성훈이 9/11에 추가함
    private int admissionYear;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<TimeTable> timetables = new ArrayList<>();

    //생성자
    public Student(String name, Department department, int grade, int admissionYear) {
        this.name = name;
        this.department = department;
        this.grade = grade;
        this.admissionYear = admissionYear;
        this.credit = new Credit();
    }

    public Student() {
    }

    public static Student from(StudentDTO studentDTO){
        Student student=new Student();
        student.setNumber(studentDTO.getNumber());
        student.setName(studentDTO.getName());
        student.setPassword(studentDTO.getPassword());
        student.setEmail(studentDTO.getEmail());
        student.setCredit(new Credit());
        return student;
    }

    //==연관관계 메서드==//
    public void addLectureToStudent(Lecture lecture, String gpa, int takesGrade, int takesSemester) {
        StudentLecture st = new StudentLecture(this,lecture, gpa, takesGrade, takesSemester);
        this.getMyLectures().add(st);
    }

    //수연 추가
    public void addStudentLecture(StudentLecture studentLecture){
        myLectures.add(studentLecture);
        studentLecture.setStudent(this);
    }

    //(gpa 인자로 안받는 경우)
    public void addLectureToStudent(Lecture lecture,int takesGrade, int takesSemester){
        addLectureToStudent(lecture,null,takesGrade,takesSemester);
    }


    //==비즈니스 로직==//

    /** 내가 들었던 강의들중 LectureName과 같은게 있다면 requiredLectures에서 삭제함*/
    public void CheckIfListenLectures( Set<String> requiredLectures ){
        Iterator<String> iterate = requiredLectures.iterator();

        while(iterate.hasNext()){
            String lectureName = iterate.next();
            if (myLectures.stream().anyMatch(sl -> (sl.getGpa()!="F" && sl.getGpa()!="NP" // F나 NP가 아닌지 확인
                    && sl.getLecture().getName().equals(lectureName))))
                    iterate.remove();
        }
    }

    /** 내가 들었던 강의들중 LectureName을 포함하는 것이 있다면 requiredLectures에서 LectureName을 포함하는 것들을 모두 삭제함*/
    public void CheckIfListenSimilarLecture(Set<String> requiredLectures, String lectureName){
        if(myLectures.stream().anyMatch(sl -> (sl.getGpa()!="F" && sl.getGpa()!="NP"    // F나 NP가 아닌지 확인
                && sl.getLecture().getName().contains(lectureName))))
            requiredLectures.removeIf(lecture -> lecture.contains(lectureName));
    }

    /**
     * 시간표 삭제
     */
//    public void deleteTimeTable(TimeTable timeTable){
//        timetables.remove(timeTable);
//    }


}
