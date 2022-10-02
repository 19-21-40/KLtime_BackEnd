package com.example.demo.controller;

import com.example.demo.Repository.*;
import com.example.demo.Service.RecommendLectureService;
import com.example.demo.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final RecommendLectureService recommendLectureService;
    private final GradConditionRepository gradConditionRepository;
    private final StudentRepository studentRepository;

    /**
     * 졸업요건 + 학점
     */
    @GetMapping("/api/gradconditionAndCredit")
    public CreditAndGradResult Credit() {

        Long studentId = 3L;

        Student student = studentRepository.findByIdWithDepartment(3L);

        GradCondition gradcondition = gradConditionRepository.findByDeptAndAdmissionYear(student.getDepartment(), student.getAdmissionYear());

        recommendLectureService.checkAndSaveCredit(3L);

        Credit credit = student.getCredit();

        GradConditionDto gradConditionDto = new GradConditionDto(gradcondition);
        CreditDto creditDto = new CreditDto(credit);

        return new CreditAndGradResult(gradConditionDto,creditDto);
    }

    /**
     * 강의 리스트
     */
    @GetMapping("/api/mainLecturelist")
    public LectureResult mainLectureList() {
        Long studentId = 3L;
        List<Lecture> lectures = recommendLectureService.recommendMainLectureWithNoDup(studentId);
        List<LectureDto> lecturelist = lectures.stream()
                .map(lecture->new LectureDto(lecture))
                .collect(toList());

        return new LectureResult(lecturelist);
    }


    @GetMapping("/api/essBalLecturelist")
    public LectureResult essBalLectureList() {
        Long studentId = 3L;
        Map<String, List<Lecture>> lectureListMap = recommendLectureService.recommendEssBalLecturesWithNoDup(studentId);
        Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
        for (String sectionDetail : lectureListMap.keySet()) {
            List<LectureDto> dtoResult = lectureListMap.get(sectionDetail).stream().map(lecture -> new LectureDto(lecture))
                    .collect(toList());
            lectureListMapDto.put(sectionDetail, dtoResult);
        }

        return new LectureResult(lectureListMapDto);
    }

    @GetMapping("/api/essLecturelist")
    public LectureResult essLectureList() {
        Long studentId = 3L;
        Map<String, List<Lecture>> lectureListMap = recommendLectureService.recommendOnlyEssLecturesWithNoDup(studentId);
        Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
        for (String sectionDetail : lectureListMap.keySet()) {
            List<LectureDto> dtoResult = lectureListMap.get(sectionDetail).stream().map(lecture -> new LectureDto(lecture))
                    .collect(toList());
            lectureListMapDto.put(sectionDetail, dtoResult);
        }

        return new LectureResult(lectureListMapDto);
    }

    @GetMapping("/api/balLecturelist")
    public LectureResult balLectureList() {
        Long studentId = 3L;
        Map<String, List<Lecture>> lectureListMap = recommendLectureService.recommendOnlyBalLecturesWithNoDup(studentId);
        Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
        for (String sectionDetail : lectureListMap.keySet()) {
            List<LectureDto> dtoResult = lectureListMap.get(sectionDetail).stream().map(lecture -> new LectureDto(lecture))
                    .collect(toList());
            lectureListMapDto.put(sectionDetail, dtoResult);
        }

        return new LectureResult(lectureListMapDto);
    }

    @GetMapping("/api/basicLecturelist")
    public LectureResult basicLectureList() {
        Long studentId = 3L;
        List<Lecture> lectures = recommendLectureService.recommendBasicLectureWithNoDup(studentId);
        List<LectureDto> lecturelist = lectures.stream()
                .map(lecture->new LectureDto(lecture))
                .collect(toList());

        return new LectureResult(lecturelist);
    }

    @GetMapping("/api/basicScienceLecturelist")
    public LectureResult basicScienceLectureList() {
        Long studentId = 3L;
        List<Lecture> lectures = recommendLectureService.recommendBasicScienceLectureWithNoDup(studentId);
        List<LectureDto> lecturelist = lectures.stream()
                .map(lecture->new LectureDto(lecture))
                .collect(toList());

        return new LectureResult(lecturelist);
    }

    @GetMapping("/api/mathLecturelist")
    public LectureResult mathLectureList() {
        Long studentId = 3L;
        List<Lecture> lectures = recommendLectureService.recommendMathLectureWithNoDup(studentId);
        List<LectureDto> lecturelist = lectures.stream()
                .map(lecture->new LectureDto(lecture))
                .collect(toList());

        return new LectureResult(lecturelist);
    }

    @Data
    @AllArgsConstructor
   static class CreditAndGradResult<A,B>{
        private A gradcondition;
        private B credit;
    }

    @Data
    @AllArgsConstructor
    static class LectureResult<T>{
        private T data;
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
        private int mathCredit; //학생의 수학영역(기초교양) 학점
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
        private int credit;

        public LectureDto(Lecture lecture) {
            name=lecture.getName();
            section=lecture.getSection();
            sectionDetail=lecture.getSectionDetail();
            level=lecture.getLevel();
            credit=lecture.getCredit();
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
            mainCredit=gradCondition.getMainCreditIfMulti();
            essBalCredit=gradCondition.getEssBalCredit();
            basicCredit=gradCondition.getBasicCredit();
        }
    }

}
