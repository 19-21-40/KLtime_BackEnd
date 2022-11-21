package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private String token;
    private int grade;
    private String number;
    private String name;
    private String password;
    private String departmentName;
    private String semester;
    private String email;
    private String multMajor;
    private String multDeptName;
}
//그럼 한번만 url 수정할테니까 delete 만 되는지 확인하고 수정할게요