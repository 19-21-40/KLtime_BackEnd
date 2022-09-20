package com.example.demo.auth;

import com.example.demo.Repository.StudentRepository;
import com.example.demo.domain.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;


    /**
     * 중복 확인 후 계정 추가
     * @param student DTO에서 가공된 student 객체
     * @return 저장된 student 객체 반환(id 생성됨)
     */
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

    public Student getByCredentials(final String number, final String password ){
        final Student originalStudent = studentRepository.findByNumberAndPassword(number,password);

//        if(originalStudent !=null && encoder.matches(password,originalStudent.getPassword())){
//            return originalStudent;
//        }
        return originalStudent;
    }


}
