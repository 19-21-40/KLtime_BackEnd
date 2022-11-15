package com.example.demo.controller;

import com.example.demo.Repository.LectureRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.Service.GraduationRequirementService;
import com.example.demo.Service.RecommendLectureService;
import com.example.demo.domain.Lecture;
import com.example.demo.domain.Student;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.StudentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend/{year}/{semester}")
public class RecommendController {

    final private StudentRepository studentRepository;
    final private RecommendLectureService recommendLectureService;
    final private LectureRepository lectureRepository;

    /**
     * 이 과목을 들은 학생들이 가장 많이 들은 과목 top3
     * @param studentDTO
     * @param year
     * @param semester
     * @param lectureNum
     * @return
     */
    @PostMapping("/lectureList1/{lectureNum}")
    public ResponseEntity<?> recommendLectureList1(
            @RequestBody StudentDTO studentDTO,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "lectureNum") String lectureNum
    ){
        try {
            Lecture lecture = lectureRepository.findByLectureNumAndYearAndSemester(lectureNum,year,semester);

            recommendLectureService.lecRecom1(lecture);

            return new ResponseEntity<>(HttpStatus.OK); //시간표 추가 후 OK 상태 반환
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /**
     * 이 과목을 들은 소프트웨어학부 n학년이 가장 많이 들은 과목 top3 (n = 해당 사용자의 학년)
     * @param studentDTO
     * @param year
     * @param semester
     * @param lectureNum
     * @return
     */
    @PostMapping("/lectureList2/{lectureNum}")
    public ResponseEntity<?> recommendLectureList2(
            @RequestBody StudentDTO studentDTO,
            @PathVariable(value = "year") int year,
            @PathVariable(value = "semester") String semester,
            @PathVariable(value = "lectureNum") String lectureNum
    ){
        try {
            Lecture lecture = lectureRepository.findByLectureNumAndYearAndSemester(lectureNum,year,semester);

            recommendLectureService.lecRecom2(lecture,studentDTO.getGrade());

            return new ResponseEntity<>(HttpStatus.OK); //시간표 추가 후 OK 상태 반환
        }catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
