package com.example.demo.auth;

import com.example.demo.domain.Student;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.StudentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class StudentController {
    private final StudentService studentService;

//    private final TokenProvider tokenProvider;

    @PostMapping("/sign_up")
    public ResponseEntity<?> registerStudent(@RequestBody StudentDTO studentDTO) {
        System.out.println("sign_up request");
        try {
            Student student=Student.from(studentDTO);

            Student registeredStudent = studentService.create(student);
            StudentDTO responseStudentDTO = StudentDTO.builder()
                    .number(registeredStudent.getNumber())
                    .name(registeredStudent.getName())
                    .email(registeredStudent.getEmail())
                    .password(registeredStudent.getPassword())
                    .build();

            return ResponseEntity.ok().body(responseStudentDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/sign_in")
    public ResponseEntity<?> authenticate(@RequestBody StudentDTO studentDTO){
        Student student = studentService.getByCredentials(
                studentDTO.getNumber(),
                studentDTO.getPassword()
        );

        if(student !=null){
//            final String token = tokenProvider.create(student);
            final StudentDTO responseStudentDTO = StudentDTO.builder()
                    .id(student.getId())
                    .number(student.getNumber())
//                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseStudentDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("로그인 실패")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
