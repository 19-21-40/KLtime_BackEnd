package com.example.demo.controller;

import com.example.demo.Repository.*;
import com.example.demo.Service.GraduationRequirementService;
import com.example.demo.domain.*;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final GraduationRequirementService graduationRequirementService;
    private final GradConditionRepository gradConditionRepository;
    private final StudentRepository studentRepository;

    /**
     * 졸업요건 + 학점
     */
    @GetMapping("/api/gradConditionAndCredit")
    public ResponseEntity<?> gradConditionAndCredit(@AuthenticationPrincipal Long id){
        try {
            Student student=studentRepository.findById(id);
//            Student student = studentRepository.findByStudentNumWithLecture(studentDTO.getNumber());
            GradCondition gradCondition= gradConditionRepository.findByDeptAndAdmissionYear(student.getDepartment(),student.getAdmissionYear());
            graduationRequirementService.checkAndSaveCredit(student.getNumber());
            Credit credit=student.getCredit();
            GradConditionDto gradConditionDto=new GradConditionDto(gradCondition);
            CreditDto creditDto=new CreditDto(credit);

            return ResponseEntity.ok().body(new CreditAndGradResult(gradConditionDto,creditDto));
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
    @GetMapping("/api/mainLectureList")
    public ResponseEntity<?> mainLectureList(@AuthenticationPrincipal Long id) {
        try{
            Student student=studentRepository.findById(id);
//            Student student=studentRepository.findByNumber(studentDTO.getNumber());

            Map<String,List<Lecture>> lectureListMap= graduationRequirementService.recommendBasicLectureWithNoDup(student.getNumber());

            Map<String,List<LectureDto>> lectureListMapDto=new HashMap<>();
            for(String s:lectureListMap.keySet()){
                List<LectureDto> lectureDtoList=lectureListMap.get(s).stream()
                        .map(LectureDto::new)
                        .collect(toList());
                lectureListMapDto.put(s,lectureDtoList);
            }

            return ResponseEntity.ok().body(new LectureResult(lectureListMapDto));
        }
        catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/api/essBalLectureList")
    public ResponseEntity<?> essBalLectureList(@AuthenticationPrincipal Long id) {
        try{
            Student student=studentRepository.findById(id);
            Map<String, List<Lecture>> lectureListMap = graduationRequirementService.recommendEssBalLecturesWithNoDup(student.getNumber());
            Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
            for (String section : lectureListMap.keySet()) {
                List<LectureDto> lecturelistDto = lectureListMap.get(section).stream()
                        .map(lecture -> new LectureDto(lecture))
                        .collect(toList());
                lectureListMapDto.put(section, lecturelistDto);
            }
            return ResponseEntity.ok().body(new LectureResult(lectureListMapDto));
        }
        catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/api/essLectureList")
    public ResponseEntity<?> essLectureList(@AuthenticationPrincipal Long id) {
        try{
            Student student=studentRepository.findById(id);
            Map<String, List<Lecture>> lectureListMap = graduationRequirementService.recommendOnlyEssLecturesWithNoDup(student.getNumber());

            Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
            for (String section : lectureListMap.keySet()) {
                List<LectureDto> lecturelistDto = lectureListMap.get(section).stream()
                        .map(lecture -> new LectureDto(lecture))
                        .collect(toList());
                lectureListMapDto.put(section, lecturelistDto);
            }
            return ResponseEntity.ok().body(new LectureResult(lectureListMapDto));
        }
        catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/api/balLectureList")
    public ResponseEntity<?> balLectureList(@AuthenticationPrincipal Long id) {
        try{
            Student student=studentRepository.findById(id);
            Map<String, List<Lecture>> lectureListMap = graduationRequirementService.recommendOnlyBalLecturesWithNoDup(student.getNumber());
            Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
            for (String section : lectureListMap.keySet()) {
                List<LectureDto> lecturelistDto = lectureListMap.get(section).stream()
                        .map(lecture -> new LectureDto(lecture))
                        .collect(toList());
                lectureListMapDto.put(section, lecturelistDto);
            }
            return ResponseEntity.ok().body(new LectureResult(lectureListMapDto));
        }
        catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/api/basicLectureList")
    public ResponseEntity<?> basicLectureList(@AuthenticationPrincipal Long id) {
        try{
            Student student=studentRepository.findById(id);
            Map<String, List<Lecture>> lectureListMap = graduationRequirementService.recommendBasicLectureWithNoDup(student.getNumber());
            Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
            for (String s : lectureListMap.keySet()) {
                List<LectureDto> lecturelistDto = lectureListMap.get(s).stream()
                        .map(lecture -> new LectureDto(lecture))
                        .collect(toList());
                lectureListMapDto.put(s, lecturelistDto);
            }
            return ResponseEntity.ok().body(new LectureResult(lectureListMapDto));
        }
        catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/api/basicScienceLectureList")
    public ResponseEntity<?> basicScienceLectureList(@AuthenticationPrincipal Long id) {
        try{
            Student student=studentRepository.findById(id);
            Map<String, List<Lecture>> lectureListMap = graduationRequirementService.recommendBasicScienceLectureWithNoDup(student.getNumber());
            Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
            for (String s : lectureListMap.keySet()) {
                List<LectureDto> lecturelistDto = lectureListMap.get(s).stream()
                        .map(lecture -> new LectureDto(lecture))
                        .collect(toList());
                lectureListMapDto.put(s, lecturelistDto);
            }
            return ResponseEntity.ok().body(new LectureResult(lectureListMapDto));
        }
        catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/api/mathLectureList")
    public ResponseEntity<?> mathLectureList(@AuthenticationPrincipal Long id) {
        try{
            Student student=studentRepository.findById(id);
            Map<String, List<Lecture>> lectureListMap = graduationRequirementService.recommendMathLectureWithNoDup(student.getNumber());
            Map<String, List<LectureDto>> lectureListMapDto = new HashMap<>();
            for (String s : lectureListMap.keySet()) {
                List<LectureDto> lecturelistDto = lectureListMap.get(s).stream()
                        .map(lecture -> new LectureDto(lecture))
                        .collect(toList());
                lectureListMapDto.put(s, lecturelistDto);
            }
            return ResponseEntity.ok().body(new LectureResult(lectureListMapDto));
        }
        catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
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
        private int essBalCredit;
        private int essCredit; //학생의 필수교양 학점
        private int balCredit; //학생의 균형교양 학점
        private int basicCredit; //학생의 기초교양 학점
        private int mathCredit; //학생의 수학영역(기초교양) 학점
        private int scienceCredit; //학생의 과학영역(기초교양) 학점

        public CreditDto(Credit credit) {
            totalCredit=credit.getTotalCredit();
            mainCredit=credit.getMainCredit();
            essBalCredit=credit.getEssBalCredit();
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
