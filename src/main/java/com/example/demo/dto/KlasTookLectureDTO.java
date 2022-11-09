package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KlasTookLectureDTO {
    String gwamokKname;
    String codeName1;
    Integer hakjumNum;
    String getGrade;
    String finishOpt;
    String hakgwa;
    String hakjungNo;
    String sungjukOpt;
    String retakeOpt;
    String termCheck;
    String termFinish;
    String retakeGetGrade;
    Integer entrytime;
}
