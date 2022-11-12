package com.example.demo.controller;


import com.example.demo.Repository.LectureRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.Repository.TimeSlotRepository;
import com.example.demo.Repository.TimeTableRepository;
import com.example.demo.Service.LectureService;
import com.example.demo.Service.TimeTableService;
import com.example.demo.domain.*;
import com.example.demo.dto.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class KlasLinkController {

    private final LectureService lectureService;

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;

    private final TimeTableService timeTableService;

    private final TimeTableRepository timetableRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Data
    @AllArgsConstructor
    static class KlasLinkDTO {

        private StudentDTO studentDTO;
        private List<KlasTookLectureListDTO> klasTookLectureListDTOList;
        private List<List<KlasTimeTableDTO>> klasTimeTableDTOListList;
    }

    @PostMapping("/api/Klas/link")
    public ResponseEntity<?> link(@RequestBody KlasLinkDTO klasLinkDTO) {
        String now = LocalDate.now().toString();
        try {

            Student student = studentRepository.findByNumber(klasLinkDTO.studentDTO.getNumber());
            klasLinkDTO.klasTookLectureListDTOList.forEach((klasTookLectureListDTO -> {
                TimeTable timeTable;

                //시간표 추가
                Long timeTable_id = timeTableService.addTimeTable(student, klasTookLectureListDTO.getThisYear(), klasTookLectureListDTO.getHakgiOrder());

                //시간표 메인시간표로 변경
                timeTable = timetableRepository.findOne(timeTable_id);
                timeTableService.changePrimary(student, klasTookLectureListDTO.getThisYear(), klasTookLectureListDTO.getHakgiOrder(), timeTable.getTableName());

                timetableRepository.findByKlasLinkedTimeTable(student, klasTookLectureListDTO.getThisYear(), klasTookLectureListDTO.getHakgiOrder())
                        .ifPresent(klasTimeTable -> timeTableService
                                .deleteTimeTable(student.getNumber(), klasTimeTable.getYearOfTimetable(), klasTimeTable.getSemester(), klasTimeTable.getTableName()));

                timeTableService.changeTimeTableName(student, timeTable.getYearOfTimetable(), timeTable.getSemester(), timeTable.getTableName(), "Klas_" + now);

                for (KlasTookLectureDTO klasTookLectureDTO : klasTookLectureListDTO.getSungjukList()) {
                    Optional<Lecture> klasLecture = lectureRepository.findByLectureNumberAndNameAndYearAndSemester(
                            klasTookLectureDTO.getHakjungNo(),
                            klasTookLectureDTO.getGwamokKname(),
                            klasTookLectureListDTO.getThisYear(),
                            klasTookLectureListDTO.getHakgiOrder());
                    if (klasLecture.isPresent()) {
                        timeTableService.addLecture(student.getNumber(), klasTookLectureListDTO.getThisYear(),
                                klasTookLectureListDTO.getHakgiOrder(), timeTable.getTableName(), klasLecture.get(), klasTookLectureDTO.getGetGrade());
                    } else {
                        KlasTimeTableDTO klasTimeTableDTO = new KlasTimeTableDTO();
                        List<TimeSlot> timeSlotList = new ArrayList<>();
                        a:
                        for (List<KlasTimeTableDTO> klasTimeTableDTOList : klasLinkDTO.klasTimeTableDTOListList) {
                            for (KlasTimeTableDTO timeTableDTO : klasTimeTableDTOList) {
                                if (timeTableDTO.getYear().equals(klasTookLectureListDTO.getThisYear().toString())
                                        && timeTableDTO.getHakgi().equals(klasTookLectureListDTO.getHakgi())) {
                                    if (timeTableDTO.getHakjungno().equals(klasTookLectureDTO.getHakjungNo())) {
                                        klasTimeTableDTO = timeTableDTO;
                                        break a;
                                    }
                                } else {
                                    continue a;
                                }
                            }
                        }

                        if(klasTimeTableDTO.getLctrumSchdulInfo()!=null) {
                            timeSlotList = klasTimeTableDTO.getTimeSlotDTO().stream().map(timeSlotDto -> {
                                return timeSlotRepository.findByTimeSlot(timeSlotDto.getDay(), timeSlotDto.getStartTime(), timeSlotDto.getEndTime())
                                        .orElseGet(() -> {
                                            return TimeSlot.from(timeSlotDto).orElseThrow(()->new IllegalStateException("error"));
                                        });
                            }).collect(Collectors.toList());
                        }
                        Lecture lecture = Lecture.createLecture(
                                klasTookLectureDTO.getHakjungNo(),
                                klasTookLectureDTO.getGwamokKname(),
                                klasTimeTableDTO.getProfNm(),
                                klasTookLectureDTO.getCodeName1(),
                                null,
                                klasTookLectureDTO.getHakjumNum(),
                                klasTookLectureDTO.getHakjungNo()!=null&&klasTookLectureDTO.getHakjungNo().length()>6?klasTookLectureDTO.getHakjungNo().charAt(5) - '0':null,
                                klasTookLectureDTO.getHakgwa(),
                                klasTookLectureListDTO.getThisYear(),
                                klasTookLectureListDTO.getHakgiOrder(),
                                timeSlotList.stream().map(timeSlot -> LectureTimeSlot.createLectureTimeSlot(timeSlot)).collect(Collectors.toList()),
                                "KLAS_AUTO_CREATE",
                                false);
                        Long lecture_id = lectureService.addCustom(lecture, timeSlotList);
                        lecture=lectureRepository.findOne(lecture_id);
                        timeTableService.addCustomLecture(student.getNumber(), klasTookLectureListDTO.getThisYear(),
                                klasTookLectureListDTO.getHakgiOrder(), timeTable.getTableName(), lecture, timeSlotList,klasTookLectureDTO.getGetGrade());
                    }
                }

            }));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
