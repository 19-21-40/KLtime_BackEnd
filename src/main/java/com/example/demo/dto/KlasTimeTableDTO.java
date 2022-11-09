package com.example.demo.dto;

import com.example.demo.controller.TimeTableController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KlasTimeTableDTO {
    String bunban;
    String codenmord;
    String grcode;
    String hakgi;
    String hakjungno;
    String isblended;
    String iselearn;
    String isonoff;
    String lctrumSchdulInfo;
    String lctrumSchdulTimeList;
    String newicon;
    String openOrganCodeNm;
    String profNm;
    String subj;
    String subjLabel;
    String subjNm;
    String year;
    String yearhakgi;



    public List<TimeTableController.TimeSlotDto> getTimeSlotDTO(){
        String[] dayNames={"월","화","수","목","금","토"};
        String[] times={"08:00","09:00","10:30","12:00","13:30","15:00","16:30","18:00","18:50","19:40","20:30","21:20","22:10","23:00"};
        Pattern pattern=Pattern.compile("([월화수목금토]) (\\d+),?(\\d+)?,?(\\d+,?)?,?(\\d+,?)?,?교시(?:/?.{0,5})");
        Matcher matcher=pattern.matcher(lctrumSchdulInfo);
        int index;
        List<TimeTableController.TimeSlotDto> timeSlotDtoList = new ArrayList<>();
        if(lctrumSchdulInfo!=null&&!lctrumSchdulInfo.isEmpty()) {
            TimeTableController.TimeSlotDto timeSlotDto;
            while (matcher.find()) {
                String dayName = matcher.group(1);
                index = Integer.parseInt(matcher.group(2));
                String startTime, endTime;
                if (index < 12) {
                    startTime = times[index];
                    endTime = times[index + 1];
                } else {
                    startTime = times[index / 10];
                    endTime = times[index % 10 + 1];
                }
                int i = 3;

                while (matcher.group(i) != null) {
                    index = Integer.parseInt(matcher.group(i));
                    if (index < 12) {
                        if (index - 1 != Integer.parseInt(matcher.group(i - 1))) {
                            timeSlotDto = new TimeTableController.TimeSlotDto(dayName, startTime, endTime);
                            timeSlotDtoList.add(timeSlotDto);
                            startTime = times[index];
                        }
                        endTime = times[index + 1];
                    } else {
                        startTime = times[index / 10];
                        endTime = times[index % 10 + 1];
                    }
                    i++;
                }
                timeSlotDto = new TimeTableController.TimeSlotDto(dayName, startTime, endTime);
                timeSlotDtoList.add(timeSlotDto);
            }
        }

        return timeSlotDtoList;
    }
}