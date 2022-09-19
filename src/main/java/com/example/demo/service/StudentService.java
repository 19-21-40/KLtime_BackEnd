package com.example.demo.Service;

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
//        validateDuplicateStudent(student);
        studentRepository.save(student);
        return student.getId();
    }

    private void validateDuplicateStudent(Student student) {
        List<Student> findStudents = studentRepository.findDupliOne(student.getId());
        if (!findStudents.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 학번(ID)입니다.");
        }
    }
    
    //나머지는 추천 알고리즘에서 쓰일 것 같음

}
