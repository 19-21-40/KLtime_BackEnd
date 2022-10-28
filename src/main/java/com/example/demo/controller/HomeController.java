package com.example.demo.controller;

import com.example.demo.Repository.*;
import com.example.demo.Service.RecommendLectureService;
import com.example.demo.domain.*;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Null;
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
//    @GetMapping("/api/gradconditionAndCredit")
//    public CreditAndGradResult Credit() {
//
//        String studentNumber = "2019203029";
//
//        Student student = studentRepository.findByStudentNumWithLecture(studentNumber);
//
//        GradCondition gradcondition = gradConditionRepository.findByDeptAndAdmissionYear(student.getDepartment(), student.getAdmissionYear());
//
//        recommendLectureService.checkAndSaveCredit(studentNumber);
//
//        Credit credit = student.getCredit();
//
//        GradConditionDto gradConditionDto = new GradConditionDto(gradcondition);
//        CreditDto creditDto = new CreditDto(credit);
//
//        return new CreditAndGradResult(gradConditionDto,creditDto);
//    }
    @PostMapping("/api/grad-conditionAndCredit")
    public ResponseEntity<?> credit(@RequestBody StudentDTO studentDTO){
        try {
            if (studentDTO.getToken() != null) {
                Student student = studentRepository.findByStudentNumWithLecture(studentDTO.getNumber());
                GradCondition gradCondition= gradConditionRepository.findByDeptAndAdmissionYear(student.getDepartment(),student.getAdmissionYear());
                recommendLectureService.checkAndSaveCredit(student.getNumber());
                Credit credit=student.getCredit();
                GradConditionDto gradConditionDto=new GradConditionDto(gradCondition);
                CreditDto creditDto=new CreditDto(credit);

                return ResponseEntity.ok().body(new CreditAndGradResult(gradConditionDto,creditDto));
            }else{
                throw new IllegalStateException("토큰이 존재하지 않습니다.");
            }
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 강의 리스트
     */
//    @GetMapping("/api/mainLecturelist")
//    public LectureResult mainLectureList() {
//        String studentNumber = "2019203029";
//
//        Map<String, List<Lecture>> lectureListMap = recommendLectureService.recommendMainLectureWithNoDup(studentNumber);
//
//        Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
//        for (String s : lectureListMap.keySet()) {
//            List<LectureDto> lecturelistDto = lectureListMap.get(s).stream()
//                    .map(lecture -> new LectureDto(lecture))
//                    .collect(toList());
//            lectureListMapDto.put(s, lecturelistDto);
//        }
//
//        return new LectureResult(lectureListMapDto);
//    }
    @PostMapping("/api/mainLectureList")
    public ResponseEntity<?> mainLectureList(@RequestBody StudentDTO studentDTO) {
        try{
            if(studentDTO.getToken()!=null){
                Student student=studentRepository.findByNumber(studentDTO.getNumber());

                Map<String,List<Lecture>> lectureListMap=recommendLectureService.recommendBasicLectureWithNoDup(student.getNumber());

                Map<String,List<LectureDto>> lectureListMapDto=new HashMap<>();
                for(String s:lectureListMap.keySet()){
                    List<LectureDto> lectureDtoList=lectureListMap.get(s).stream()
                            .map(LectureDto::new)
                            .collect(toList());
                    lectureListMapDto.put(s,lectureDtoList);
                }

                return ResponseEntity.ok().body(new LectureResult(lectureListMapDto));
            }
            else{
                throw new IllegalStateException("hello");
            }
        }
        catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @GetMapping("/api/essBalLecturelist")
    public LectureResult essBalLectureList() {
        String studentNumber = "2019203029";

        Map<String, List<Lecture>> lectureListMap = recommendLectureService.recommendEssBalLecturesWithNoDup(studentNumber);

        Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
        for (String section : lectureListMap.keySet()) {
            List<LectureDto> lecturelistDto = lectureListMap.get(section).stream()
                    .map(lecture -> new LectureDto(lecture))
                    .collect(toList());
            lectureListMapDto.put(section, lecturelistDto);
        }

        return new LectureResult(lectureListMapDto);
    }

    @GetMapping("/api/essLecturelist")
    public LectureResult essLectureList() {
        String studentNumber = "2019203029";

        Map<String, List<Lecture>> lectureListMap = recommendLectureService.recommendOnlyEssLecturesWithNoDup(studentNumber);

        Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
        for (String section : lectureListMap.keySet()) {
            List<LectureDto> lecturelistDto = lectureListMap.get(section).stream()
                    .map(lecture -> new LectureDto(lecture))
                    .collect(toList());
            lectureListMapDto.put(section, lecturelistDto);
        }

        return new LectureResult(lectureListMapDto);
    }

    @GetMapping("/api/balLecturelist")
    public LectureResult balLectureList() {
        String studentNumber = "2019203029";
        Map<String, List<Lecture>> lectureListMap = recommendLectureService.recommendOnlyBalLecturesWithNoDup(studentNumber);
        Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
        for (String section : lectureListMap.keySet()) {
            List<LectureDto> lecturelistDto = lectureListMap.get(section).stream()
                    .map(lecture -> new LectureDto(lecture))
                    .collect(toList());
            lectureListMapDto.put(section, lecturelistDto);
        }

        return new LectureResult(lectureListMapDto);
    }

    @GetMapping("/api/basicLecturelist")
    public LectureResult basicLectureList() {
        String studentNumber = "2019203029";

        Map<String, List<Lecture>> lectureListMap = recommendLectureService.recommendBasicLectureWithNoDup(studentNumber);

        Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
        for (String s : lectureListMap.keySet()) {
            List<LectureDto> lecturelistDto = lectureListMap.get(s).stream()
                    .map(lecture -> new LectureDto(lecture))
                    .collect(toList());
            lectureListMapDto.put(s, lecturelistDto);
        }

        return new LectureResult(lectureListMapDto);
    }
    @GetMapping("/api/basicScienceLecturelist")
    public LectureResult basicScienceLectureList() {
        String studentNumber = "2019203029";

        Map<String, List<Lecture>> lectureListMap = recommendLectureService.recommendBasicScienceLectureWithNoDup(studentNumber);

        Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
        for (String s : lectureListMap.keySet()) {
            List<LectureDto> lecturelistDto = lectureListMap.get(s).stream()
                    .map(lecture -> new LectureDto(lecture))
                    .collect(toList());
            lectureListMapDto.put(s, lecturelistDto);
        }

        return new LectureResult(lectureListMapDto);
    }

    @GetMapping("/api/mathLecturelist")
    public LectureResult mathLectureList() {
        String studentNumber = "2019203029";

        Map<String, List<Lecture>> lectureListMap = recommendLectureService.recommendMathLectureWithNoDup(studentNumber);

        Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
        for (String s : lectureListMap.keySet()) {
            List<LectureDto> lecturelistDto = lectureListMap.get(s).stream()
                    .map(lecture -> new LectureDto(lecture))
                    .collect(toList());
            lectureListMapDto.put(s, lecturelistDto);
        }

        return new LectureResult(lectureListMapDto);
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
