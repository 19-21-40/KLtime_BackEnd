package com.example.demo.controller;

import com.example.demo.Repository.LectureSearch;
import com.example.demo.domain.Lecture;
import com.example.demo.Service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/lectures")
    public String lectureList(@ModelAttribute("lectureSearch") LectureSearch lectureSearch, Model model ) {

        List<Lecture> lectures = lectureService.findLectures(lectureSearch);
        model.addAttribute("lectures", lectures);
        return "lecture/lectureList";
    }
}
