package com.example.demo.controller;

import com.example.demo.Repository.*;
import com.example.demo.Service.RecommendLectureService;
import com.example.demo.domain.Credit;
import com.example.demo.domain.Department;
import com.example.demo.domain.GradCondition;
import com.example.demo.domain.Lecture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class HomeController {

    //private final (연관관계 주입)

    /**
     * 졸업요건 + 학점
     */
    @GetMapping("/api/GradConditionAndCredit")
    public Result mainCredit() {
        List<GradCondition> Gradconditions = a;
        List<GradConditionDto> collectGradCondition = Gradconditions.stream()
                .map(gradCondition->new GradConditionDto(gradCondition))
                .collect(toList());

        List<Credit> Credits = b;
        List<CreditDto> collectCredit = Credits.stream()
                .map(credit->new CreditDto(credit))
                .collect(toList());

        return new Result(collectGradCondition,collectCredit);
    }

    /**
     * 강의 리스트
     */
    @GetMapping("/api/lecturelist")
    public List<LectureDto> LectureList() {
        List<Lecture> lectures = c;
        List<LectureDto> lecturelist = lectures.stream()
                .map(lecture->new LectureDto(lecture))
                .collect(toList());

        return lecturelist;
    }

    @Data
    @AllArgsConstructor
   static class Result<A,B>{
        private A gradcondition;
        private B credit;
    }

    @Data
    @AllArgsConstructor
    static class CreditDto {
        private int totalCredit; //학생의 총학점
        private int mainCredit; //학생의 주전공 학점
        private int multiCredit; //학생의 복수전공 학점
        private int essCredit; //학생의 필수교양 학점
        private int balCredit; //학생의 균형교양 학점
        private int basicCredit; //학생의 기초교양 학점
        private int mathCredit; //학생의 수학ㄷ영역(기초교양) 학점
        private int scienceCredit; //학생의 과학영역(기초교양) 학점

        public CreditDto(Credit credit) {
            totalCredit=credit.getTotalCredit();
            mainCredit=credit.getMainCredit();
            essCredit=credit.getEssCredit();
            balCredit=credit.getBalCredit();
            basicCredit=credit.getBasicCredit();
            mathCredit=credit.getMathCredit();
            scienceCredit=credit.getScienceCredit();
        }
    }

    @Data
    @AllArgsConstructor
    static class LectureDto {
        private String name;
        private String section;
        private String sectionDetail;
        private int level;
        private String departmentName;
        private String notes;

        public LectureDto(Lecture lecture) {
            name=lecture.getName();
            section=lecture.getSection();
            sectionDetail=lecture.getSectionDetail();
            level=lecture.getLevel();
            departmentName=lecture.getDepartmentName();
            notes=lecture.getNotes();
        }
    }

    @Data
    @AllArgsConstructor
    static class GradConditionDto {

        private int admissionYear;
        private int gradCredit;
        private int mainCredit;
        private int essBalCredit;
        private int basicCredit;
        private Integer multiCredit;

        public GradConditionDto(GradCondition gradCondition) {
            admissionYear=gradCondition.getAdmissionYear();
            gradCredit=gradCondition.getGradCredit();
            mainCredit=gradCondition.getMainCredit();
            essBalCredit=gradCondition.getEssBalCredit();
            basicCredit=gradCondition.getMultiCredit();
        }
    }

}
