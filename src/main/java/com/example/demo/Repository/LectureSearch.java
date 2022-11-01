package com.example.demo.Repository;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LectureSearch {
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
}
