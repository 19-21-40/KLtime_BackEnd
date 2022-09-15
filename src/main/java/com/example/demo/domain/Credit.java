package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Getter
public class Credit {

    private int totalCredit; // 학생의 총학점
    private int mainCredit; // 학생의 주전공 학점
    private int multiCredit; // 학생의 복수전공 학점
    private int essCredit; // 학생의 필수교양 학점
    private int balCredit; // 학생의 균형교양 학점
    private int basicCredit; // 학생의 기초교양 학점
    private int mathCredit; // 학생의 수학영역(기초교양) 학점
    private int scienceCredit; // 학생의 과학영역(기초교양) 학점

    public Credit() {
        totalCredit = 0;
        mainCredit = 0;
        multiCredit = 0;
        essCredit = 0;
        balCredit = 0;
        basicCredit = 0;
        mathCredit = 0;
        scienceCredit = 0;

    }



    public void addTotalCredit(int credit) {
        this.totalCredit += credit;
    }

    public void addMainCredit(int credit) {
        this.mainCredit += credit;
    }

    public void addMultiCredit(int credit) {
        this.multiCredit += credit;
    }

    public void addEssCredit(int credit) { this.essCredit += credit; }

    public void addBalCredit(int credit) {
        this.balCredit += credit;
    }

    public void addBasicCredit(int credit) {
        this.basicCredit += credit;
    }

    public void addMathCredit(int credit) {
        this.mathCredit += credit;
    }

    public void addScienceCredit(int credit) {
        this.scienceCredit += credit;
    }

}
