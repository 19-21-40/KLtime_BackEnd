package com.example.demo.auth;

import com.example.demo.Repository.StudentRepository;
import com.example.demo.domain.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Student create(final Student student){
        if(student ==null || student.getNumber()==null){
            throw new RuntimeException("인자가 올바르지 않습니다.");
        }
        final String number=student.getNumber();
        if(studentRepository.existsByNumber(number)){
            throw new RuntimeException("해당 학번은 이미 존재합니다.");
        }

        return studentRepository.save(student);
    }

    public Student getByCredentials(final String number, final String password){
        return studentRepository.findByNumberAndPassword(number,password);
    }


}
