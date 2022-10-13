package com.example.demo.auth;

import com.example.demo.Repository.DepartmentRepository;
import com.example.demo.domain.Department;
import com.example.demo.domain.Student;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.StudentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.NoResultException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class StudentController {
    private final StudentService studentService;
    private final DepartmentRepository departmentRepository;
    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder =new BCryptPasswordEncoder();

    @PostMapping("/sign_up")
    public ResponseEntity<?> registerStudent(@RequestBody StudentDTO studentDTO) {
        try {
            Student student=Student.from(studentDTO)
                    .orElseThrow(()->{throw new IllegalStateException("지정된 형식과 일치하지 않습니다.");});
            student.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
            Department department=departmentRepository.findByName(studentDTO.getDepartmentName())
                    .orElseThrow(()->{throw new IllegalStateException("해당학부가 존재하지 않습니다.");});
            student.setDepartment(department);
            Student registeredStudent = studentService.create(student);
            StudentDTO responseStudentDTO = StudentDTO.builder()
                    .grade(studentDTO.getGrade())
                    .number(registeredStudent.getNumber())
                    .name(registeredStudent.getName())
                    .email(registeredStudent.getEmail())
                    .build();
            return ResponseEntity.ok().body(responseStudentDTO);
        }
        catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/sign_in")
    public ResponseEntity<?> authenticate(@RequestBody StudentDTO studentDTO){
        Student student = studentService.getByCredentials(
                studentDTO.getNumber(),
                studentDTO.getPassword(),
                passwordEncoder
        );

        if(student !=null){
            final String token = tokenProvider.create(student);
            final StudentDTO responseStudentDTO = StudentDTO.builder()
                    .number(student.getNumber())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseStudentDTO);
        } else {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error("로그인 실패")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
