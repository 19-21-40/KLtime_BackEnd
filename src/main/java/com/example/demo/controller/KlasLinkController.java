package com.example.demo.controller;


import com.example.demo.Service.LectureService;
import com.example.demo.Service.TimeTableService;
import com.example.demo.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KlasLinkController {

    private final LectureService lectureService;

    private final TimeTableService timeTableService;

    @PostMapping("/api/Klas/link")
    public ResponseEntity<?> link(@RequestBody ResponseDTO responseDTO){
        if(responseDTO.getData()!=null){
            System.out.println(responseDTO.getData());
        }
        return ResponseEntity.ok().body(responseDTO);
    }
}
