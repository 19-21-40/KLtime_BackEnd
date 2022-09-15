package com.example.demo.service;

import com.example.demo.Repository.StudentRepository;
import com.example.demo.domain.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Student student){
        studentRepository.save(student);
        return student.getId();
    }
    
    //나머지는 추천 알고리즘에서 쓰일 것 같음

}
