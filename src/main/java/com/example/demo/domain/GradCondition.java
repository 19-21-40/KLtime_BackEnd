package com.example.demo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//protected default constructor
public class GradCondition {

    @Id
    @GeneratedValue
    @Column(name = "gradcondition_id")
    private Long id;
    private int admissionYear;
    private int gradCredit;
    private int essBalCredit;
    private int basicCredit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;
    private int mainCredit;
    private boolean isMultiDept;
    private Integer multiCredit;

    public GradCondition(int admissionYear, int gradCredit, int essBalCredit, int basicCredit, Department department, int mainCredit, boolean isMultiDept) {
        this.admissionYear = admissionYear;
        this.gradCredit = gradCredit;
        this.essBalCredit = essBalCredit;
        this.basicCredit = basicCredit;
        this.department = department;
        this.mainCredit = mainCredit;
        this.isMultiDept = isMultiDept;
    }
}
