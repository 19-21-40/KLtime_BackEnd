package com.example.demo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class GradCondition {

    @Id
    @GeneratedValue
    @Column(name = "gradcondition_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;

    private int admissionYear;
    private int essBalCredit;
    private Integer basicCredit;
    private int mainCreditIfNotMulti;  // 주전공학점(단일전공시)
    private int mainCreditIfMulti;  // 주전공학점(복수전공시)
    private Integer deepMajorCredit;  // 심화전공학점
    private int doubleMajorCredit;  // 복수전공학점
    private int minorCredit;  // 부전공학점

    private int gradCredit;

    private int mathCredit;

    private int scienceCredit;

    @ElementCollection
    private List<String> requiredLectures;

    public GradCondition() {

    }

    public GradCondition(Department department, int admissionYear, int essBalCredit, Integer basicCredit, int mainCreditIfNotMulti, int mainCreditIfMulti, Integer deepMajorCredit, int doubleMajorCredit, int minorCredit, int gradCredit) {
        this.department = department;
        this.admissionYear = admissionYear;
        this.essBalCredit = essBalCredit;
        this.basicCredit = basicCredit;
        this.mainCreditIfNotMulti = mainCreditIfNotMulti;
        this.mainCreditIfMulti = mainCreditIfMulti;
        this.deepMajorCredit = deepMajorCredit;
        this.doubleMajorCredit = doubleMajorCredit;
        this.minorCredit = minorCredit;
        this.gradCredit = gradCredit;
    }
}
