package com.example.demo.Service;

import com.example.demo.Repository.DepartmentRepository;
import com.example.demo.Repository.GradConditionRepository;
import com.example.demo.Repository.LectureRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendLectureService {

    private final StudentRepository studentRepository;
    private final GradConditionRepository gradConditionRepository;
    private final LectureRepository lectureRepository;
    private final DepartmentRepository departmentRepository;



    public void recommendLecture() {

        // 학생의 학번과 학과 불러오기 // 임의로 데이터베이스에 저장된 Id=3인 "이성훈"을 불러옴
        Student student = studentRepository.findByIdWithLecture(3L);
        Department department = departmentRepository.findOne(student.getDepartment().getId());
        int mainCredit = 0; // 학생의 mainCredit을 계산하기 위해 초기화
        int essCredit = 0; // 학생의 essCredit을 계산하기 위해 초기화
        int balCredit = 0; // 학생의 balCredit을 계산하기 위해 초기화
        int basicCredit = 0; // 학생의 basicCredit을 계산하기 위해 초기화
        int gradeCredit = 0; // 학생의 gradeCredit을 계산하기 위해 초기화

        // 그에 맞는 졸업 학점 조건 불러오기
        if(student.getMultiDept() == null){
            GradCondition gradCondition = gradConditionRepository.findByDeptAndAdmissionYearWithNoMultiDept(department, student.getAdmissionYear());
        } else {
            GradCondition gradCondition = gradConditionRepository.findByDeptAndAdmissionYearWithMultiDept(department, student.getAdmissionYear());
        }

        // 학생에게 필요한 (교필,기필)과목리스트를 String으로 저장
        List<String> reqLec = findRequiredLectures();

        // 학생이 들었던 과목 리스트
        List<StudentLecture> myLectures = student.getMyLectures();

        // 테스트 출력
        for (StudentLecture myLecture : myLectures) {
            System.out.println(myLecture);
        }

        // 필수+균형교양(광운인되기, 대학영어, 정보, 과학과기술, 예술과체육 등)에 대한 학생의 이수학점을 기록할 변수
        Map<String, Integer> map = new HashMap<>();

        map.put("광운인되기", 0);
        map.put("대학영어", 0);
        map.put("정보", 0);
        map.put("융합적사고와글쓰기", 0);
        map.put("과학과기술", 0);
        map.put("인간과철학", 0);
        map.put("사회와경제", 0);
        map.put("글로벌문화와제2외국어", 0);
        map.put("예술과체육", 0);

        // 학생의 totalCredit을 0으로 초기화 ( Student 엔티티안에 따로 만들어주면 굳이 초기화할 필요없긴 함 )
        student.setTotalCredit(0);

        // 내가 들었던 강의들에 대해 for문을 돌림
        for (StudentLecture sl : myLectures) {


            // 코드를 짧게 작성하기 위해 따로 초기화해줬음
            Lecture lecture = sl.getLecture();
            String section = lecture.getSection();
            String sectionDetail = lecture.getSectionDetail();


            // 받은 학점이 F or NP라면 포함하지 않음 + continue 실행
            if(sl.getGpa() == "F" || sl.getGpa() == "NP") continue;

            // 그게 아니라면
            // student.totalCredit에 현재강의의 학점을 더해줌
            student.addCredit(lecture.getCredit());

            gradeCredit += lecture.getCredit();

            // 교필과 교선에 대해서 먼저 따져줌 ( sectionDetail에 따라 처리해줘야 함 )
            if(section == "교필" || section == "교선")
            {
                switch (sectionDetail){
                    case "광운인되기":
                        // 광운인되기 강의는 학점이 1으로 고정이면서 강의가 1개이므로 그냥 put
                        map.put("광운인되기", 1);
                        essCredit += 3;
                        break;
                    case "대학영어": case "정보":
                        // map 컬렉션의 value에 현재 강의의 credit을 더해줌
                        map.compute(sectionDetail, (s,v) -> v + lecture.getCredit());
                        essCredit += 3;
                        break;
                    case "융합적사고와글쓰기":
                        // 융사 강의는 학점이 3으로 고정이면서 강의가 1개이므로 그냥 put
                        map.put(sectionDetail, 3);
                        // 단과대학이나 학과에 따라 균형교양, 필수교양으로 갈림. 그에 따라 코드 작성했음
                        if(student.getDepartment().getName() == "경영학부" || student.getDepartment().getName() == "국제통상학부" ||
                                student.getDepartment().getCollegeName() == "자연대" || student.getDepartment().getCollegeName() == "소융대")
                            balCredit += 3;
                        else
                            essCredit += 3;
                        break;
                    case "과학과기술": case "인간과철학": case "사회와경제": case "글로벌문화와제2외국어":
                        // map 컬렉션의 value에 현재 강의의 credit을 더해줌
                        map.compute(sectionDetail, (s,v) -> v+ lecture.getCredit());
                        balCredit += 3;
                        break;
                    default:
                        /** 여기에 에러 출력하는 코드 작성해야함 */
                        break;
                }
            }


            // 전공학점에 대해 따짐
            if (section == "전필" || section == "전선") {
                mainCredit += lecture.getCredit();
            }

            // 기초교양학점에 대해 따짐
            if (section == "기필" || section == "기선") {
                basicCredit +=lecture.getCredit();
            }


            // 필수 교양들에 대해서 처리해줌, 만약 내가 들었던 강의 목록에 있다면 reqLec에서 제거함. 현재 아래 코드는 19학번 소프트웨어학부 기준임. 리팩토링 필요
            if(lecture.getName() == "광운인되기") reqLec.remove("광운인되기");
            if(lecture.getName() == "대학영어") reqLec.remove("대학영어");
            if(lecture.getName() == "C프로그래밍") reqLec.remove("C프로그래밍");
            if(lecture.getName() == "컴퓨팅사고") reqLec.remove("컴퓨팅사고");
            if(lecture.getName() == "고급C프로그래밍및설계") reqLec.remove("고급C프로그래밍및설계");
            if(lecture.getName() == "공학설계입문") reqLec.remove("공학설계입문");
            if(lecture.getName().contains("및실험")){
                reqLec = reqLec.stream().filter( o -> !o.contains("및실험")).collect(Collectors.toList());
            }
            
            // 교과목들 이름 변경된거 체크 필수. ex) 읽기와쓰기, 말하기와소통 -> 융합적사고와글쓰기
            // 또한 대학영어, 융사는 단과대학에 따라서 듣는 학기가 정해져있음

        }

        System.out.println(map);
        System.out.println("balCredit=" + balCredit + " essCredit=" + essCredit + " mainCredit=" + mainCredit + " basicCredit=" + basicCredit);

        System.out.println(reqLec);

        // 학점이 얼마나 부족한지 각각 정리





        // 그에 맞는 과목들 정리

        //필수_전공_과목들_수강하였는지_체크();
    }

    private List<String> findRequiredLectures() {

        // 19학번 소프트웨어학부 기준으로
        List<String> reqLec = new ArrayList<>();

        reqLec.add("광운인되기");
        reqLec.add("대학영어");
        reqLec.add("C프로그래밍");
        reqLec.add("컴퓨팅사고");
        reqLec.add("고급C프로그래밍및설계");
        reqLec.add("공학설계입문");

        reqLec.add("대학물리및실험1");
        reqLec.add("대학물리및실험2");
        reqLec.add("대학화학및실험1");    // 하나라도 수강한다면 나머지를 pop할 예정
        reqLec.add("대학화학및실험2");

        return reqLec;
    }

}
