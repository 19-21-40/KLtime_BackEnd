package com.example.demo.domain;

import com.example.demo.controller.TimeTableController;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@Entity
@Getter
@Setter
public class Lecture {

    //semester int->string 변경

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long id;

    private String lectureNumber;

    private String name;
    private String professor;
    private String section;
    private String sectionDetail;
    private Integer credit;
    private Integer level;
    private String category;
    private Integer yearOfLecture;
    private String semester;
    private String notes;

    private boolean isCustom;


    @OneToMany(mappedBy = "lecture",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<LectureTimeSlot> times = new ArrayList<>();

    @OneToMany(mappedBy = "lecture",cascade = CascadeType.ALL)
    private List<TimeTableLecture> tableListWhichAdd = new ArrayList<>();


    protected Lecture(){

    }
    public Lecture(String lectureNumber, String name) {
        this.lectureNumber = lectureNumber;
        this.name = name;
    }

    public Lecture(String lectureNumber, String name, String section, String sectionDetail, Integer credit, String professor, String category, Integer level, Integer yearOfLecture, String semester, String notes, boolean isCustom) {
        this.lectureNumber = lectureNumber;
        this.name = name;
        this.professor = professor;
        this.section = section;
        this.sectionDetail = sectionDetail;
        this.credit = credit;
        this.level = level;
        this.category = category;
        this.yearOfLecture = yearOfLecture;
        this.semester = semester;
        this.notes = notes;
        this.isCustom = isCustom;
    }


    /**
     * LectureTimeSlot 추가
     * createLectureTimeSlot 을 통해 우선적으로 LectureTimeSlot 객체를 만들어 사용
     * @param lts LectureTimeSlot 객체
     */
    public void addTimes(LectureTimeSlot lts) {
        if(!times.contains(lts)){
            this.times.add(lts);
            lts.setLecture(this); //굳이 여기서 연관관계 메서드 2번 할 필요 X (LectureTimeSlot의 setLecture 주석 처리 함)
        }
    }

    public void addLectureTimeSlot(LectureTimeSlot lectureTimeSlot){
        times.add(lectureTimeSlot);
        lectureTimeSlot.setLecture(this);
    }

    //LectureDto -> Lecture 바꾸는 함수 (커스텀 강의 만들 때만 쓰임)
    public static Optional<Lecture> from(TimeTableController.LectureDto lectureDto,List<TimeSlot> timeSlots){
        Lecture lecture = createCustomLecture(
                lectureDto.getId(),
                lectureDto.getLectureName(),
                lectureDto.getProfessor(),
                lectureDto.getSection(),
                lectureDto.getSectionDetail(),
                lectureDto.getCredit(),
                lectureDto.getLevel(),
                lectureDto.getDepartment(),
                lectureDto.getYearOfLecture(),
                lectureDto.getSemester(),
                lectureDto.getNotes(),
                timeSlots.stream().map(timeSlot -> LectureTimeSlot.createLectureTimeSlot(timeSlot)).collect(Collectors.toList())
        ); //수정(수연)
        return Optional.of(lecture);
    }
    
    public void setTimes(List<LectureTimeSlot> ltsList){
        this.times=ltsList;
        ltsList.forEach(lts->lts.setLecture(this));
    }

    
    //==생성 메서드==//
    public static Lecture createLecture(
            String lectureNumber,
            String name,
            String professor,
            String section,
            String sectionDetail,
            Integer credit,
            Integer level,
            String category,
            Integer yearOfLecture,
            String semester,
            List<LectureTimeSlot> times, //추가(수연)
            String notes,
            boolean isCustom
            ){

        Lecture lecture=new Lecture();
        lecture.setLectureNumber(lectureNumber);
        lecture.setName(name);
        lecture.setProfessor(professor);
        lecture.setSection(section);
        lecture.setSectionDetail(sectionDetail);
        lecture.setCredit(credit);
        lecture.setLevel(level);
        lecture.setCategory(category);
        lecture.setYearOfLecture(yearOfLecture);
        lecture.setSemester(semester);
        if(times!=null) {
            for (LectureTimeSlot lectureTimeSlot : times) {
                lecture.addTimes(lectureTimeSlot);
            }
        }else{
            lecture.setTimes(new ArrayList<>());
        }
        lecture.setNotes(notes);
        lecture.setCustom(isCustom);

        return lecture;
    }


    //추가(수연)
    //==커스텀강의 생성 메서드==//
    public static Lecture createCustomLecture(
            String lectureNumber,
            String name,
            String professor,
            String section,
            String sectionDetail,
            Integer credit,
            Integer level,
            String category,
            Integer yearOfLecture,
            String semester,
            String notes,
            List<LectureTimeSlot> lectureTimeSlots){
        return createLecture(lectureNumber,name,professor,section,sectionDetail,credit,level,category,yearOfLecture,semester,lectureTimeSlots,notes,true);
    }
    
    @Override
    public String toString() {
        return "LectureNumber = " + lectureNumber + ", Name = " + name + ", Professor = " + professor;
    }

}
