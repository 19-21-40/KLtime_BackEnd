package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KlasTookLectureListDTO {
    String hakgi;
    String hakgiOrder;
    List<KlasTookLectureDTO> sungjukList;
    String thisYear;

    public String getHakgiOrder() {
        if (Integer.parseInt(hakgi) < 3)
            return hakgiOrder + "학기";
        else return hakgiOrder;
    }

    public Integer getThisYear() {
        return Integer.parseInt(thisYear);
    }
}

