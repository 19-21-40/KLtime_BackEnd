package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Department {

    @Id @GeneratedValue
    @Column(name = "dept_id")
    private Long id;
    private String name;
    private String collegeName;

    public Department(String name, String collegeName) {
        this.name = name;
        this.collegeName = collegeName;
    }

    public Department() {
    }
}
