package com.example.demo.Service;

import com.example.demo.Repository.*;
import com.example.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendLectureService {

    private final StudentRepository studentRepository;
    private final GradConditionRepository gradConditionRepository;
    private final LectureRepository lectureRepository;
    private final DepartmentRepository departmentRepository;


    public void checkAndSaveCredit(Long studentId) {

        Student student = studentRepository.findByIdWithLecture(studentId);

        // 학생의 학번과 학과 불러오기 // 임의로 데이터베이스에 저장된 Id=3인 "이성훈"을 불러옴
        Department department = departmentRepository.findOne(student.getDepartment().getId());

        // 그에 맞는 졸업 학점 조건 불러오기
        GradCondition gradCondition = new GradCondition();

        if (student.getMultiDept() == null) {
            gradCondition = gradConditionRepository.findByDeptAndAdmissionYearWithNoMultiDept(department, student.getAdmissionYear());
        } else {
            gradCondition = gradConditionRepository.findByDeptAndAdmissionYearWithMultiDept(department, student.getAdmissionYear());
        }

        Credit temporalCredit = new Credit();

        // 학생이 들었던 과목 리스트
        List<StudentLecture> myLectures = student.getMyLectures();

        /** section과 sectionDetail에 따라 학점을 각각 관리하는 코드 */
        // 필수+균형교양(광운인되기, 대학영어, 정보, 과학과기술, 예술과체육 등)에 대한 학생의 이수학점을 기록할 변수


        // 내가 들었던 강의들에 대해 for문을 돌림
        for (StudentLecture sl : myLectures) {

            // 코드를 짧게 작성하기 위해 따로 초기화해줬음
            Lecture lecture = sl.getLecture();
            String section = lecture.getSection();
            String sectionDetail = lecture.getSectionDetail();

            // 받은 학점이 F or NP라면 continue 실행
            if (sl.getGpa() == "F" || sl.getGpa() == "NP") continue;

            // 그게 아니라면
            // totalCredit에 현재강의의 학점을 더해줌
            temporalCredit.addTotalCredit(lecture.getCredit());


            /** 19학번 기준 */
            // 교필과 교선에 대해서 먼저 따져줌 ( sectionDetail에 따라 처리해줘야 함 )
            if (section == "교필" || section == "교선") {
                switch (sectionDetail) {
                    case "광운인되기":
                        temporalCredit.addEssCredit(1);
                        break;
                    case "대학영어":
                    case "정보":
                        temporalCredit.addEssCredit(3);
                        break;
                    case "융합적사고와글쓰기":
                        // 단과대학이나 학과에 따라 균형교양, 필수교양으로 갈림. 그에 따라 코드 작성했음
                        if (student.getDepartment().getName() == "경영학부" || student.getDepartment().getName() == "국제통상학부" ||
                                student.getDepartment().getCollegeName() == "자연대" || student.getDepartment().getCollegeName() == "소융대")
                            temporalCredit.addBalCredit(3);
                        else {
                            temporalCredit.addEssCredit(3);
                        }
                        break;
                    case "과학과기술":
                    case "인간과철학":
                    case "사회와경제":
                    case "글로벌문화와제2외국어":
                        temporalCredit.addBalCredit(lecture.getCredit());
                        break;
                    default:
                        /** 여기에 에러 출력하는 코드 작성해야함 */
                        break;
                }
            }

            // 전공학점에 대해 따짐
            if (section == "전필" || section == "전선") {
                temporalCredit.addMainCredit(lecture.getCredit());
            }

            // 기초교양학점에 대해 따짐
            if (section == "기필" || section == "기선") {
                temporalCredit.addBasicCredit(lecture.getCredit());
                if (sectionDetail == "수학")
                    temporalCredit.addMathCredit(lecture.getCredit());
                if (sectionDetail == "기초과학") {
                    temporalCredit.addScienceCredit(lecture.getCredit());
                }
            }

            // 교과목들 이름 변경된거 체크 필수. ex) 읽기와쓰기, 말하기와소통 -> 융합적사고와글쓰기
            // 또한 대학영어, 융사는 단과대학에 따라서 듣는 학기가 정해져있음

        }
        System.out.println("---------------------------------------------");
        System.out.println(temporalCredit.getTotalCredit() + ", " + temporalCredit.getBalCredit());

        student.setCredit(temporalCredit);

        System.out.println(student.getCredit().getTotalCredit());


        // 학점이 얼마나 부족한지 각각 정리


        // 그에 맞는 과목들 정리

        //필수_전공_과목들_수강하였는지_체크();
    }


    private Set<String> setRequiredLecture() {

        // 19학번 소프트웨어학부 기준으로
        Set<String> reqLec = new HashSet<>();


        // 교필 또는 기필에 대해서 reqLec에 추가해줌
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


        // 전공에 대해서 reqLec에 추가해줌
        List<Lecture> mainLectures = lectureRepository.findByTwoSection("전필", "전선"); // 전필, 전선 강의들
        for (Lecture lecture : mainLectures) {
            reqLec.add(lecture.getName());
        }

        return reqLec;
    }


    public Set<String> computeRequiredLecture(Student student) {

        // 학생에게 필요한 과목(교필, 기필, 전필, 전선)리스트를 String으로 저장
        /** 추후에 db에서 불러오는 것으로 생각 */
        Set<String> req_lec = setRequiredLecture();

        /** 19학번 소프트웨어학부 기준 */
        // 필수 교양들에 대해서 처리해줌, 만약 내가 들었던 강의 목록에 있다면 reqLec에서 제거함.
        student.CheckIfListenLectures(req_lec);
        // 실험과목 1개라도 들었을 경우, reqLec에서 "및실험"을 포함한 과목을 remove함
        student.CheckIfListenSimilarLecture(req_lec, "및실험");

        return req_lec;
    }

    public void recommendLectures(Student student, Set<String> req_lec) {
        List<Lecture> recommendList = new ArrayList<>();
        for (String lectureName : req_lec) {
            Lecture lecture = lectureRepository.findByLectureName(lectureName);
            recommendList.add(lecture);
        }


        /** section과 sectionDetail에 따라 학점을 각각 관리하는 코드 */
        // 필수+균형교양(광운인되기, 대학영어, 정보, 과학과기술, 예술과체육 등)에 대한 학생의 이수학점을 기록할 변수
        Map<String, Integer> essBalMap = new HashMap<>();

        essBalMap.put("광운인되기", 0);
        essBalMap.put("대학영어", 0);
        essBalMap.put("정보", 0);
        essBalMap.put("융합적사고와글쓰기", 0);
        essBalMap.put("과학과기술", 0);
        essBalMap.put("인간과철학", 0);
        essBalMap.put("사회와경제", 0);
        essBalMap.put("글로벌문화와제2외국어", 0);
        essBalMap.put("예술과체육", 0);


        // 학생의 학번과 학과 불러오기 // 임의로 데이터베이스에 저장된 Id=3인 "이성훈"을 불러옴
        Department department = departmentRepository.findOne(student.getDepartment().getId());

        // 그에 맞는 졸업 학점 조건 불러오기
        GradCondition gradCondition = new GradCondition();

        if (student.getMultiDept() == null) {
            gradCondition = gradConditionRepository.findByDeptAndAdmissionYearWithNoMultiDept(department, student.getAdmissionYear());
        } else {
            gradCondition = gradConditionRepository.findByDeptAndAdmissionYearWithMultiDept(department, student.getAdmissionYear());
        }

        // 학생이 들었던 과목 리스트
        List<StudentLecture> myLectures = student.getMyLectures();

        // 내가 들었던 강의들에 대해 for문을 돌림
        for (StudentLecture sl : myLectures) {

            // 코드를 짧게 작성하기 위해 따로 초기화해줬음
            Lecture lecture = sl.getLecture();
            String section = lecture.getSection();
            String sectionDetail = lecture.getSectionDetail();

            // 받은 학점이 F or NP라면 continue 실행
            if (sl.getGpa() == "F" || sl.getGpa() == "NP") continue;


            /** 19학번 기준 */
            // 교필과 교선에 대해서 먼저 따져줌 ( sectionDetail에 따라 처리해줘야 함 )
            if (section == "교필" || section == "교선") {
                switch (sectionDetail) {
                    case "광운인되기":
                        // 광운인되기 강의는 학점이 1으로 고정이면서 강의가 1개이므로 그냥 put
                        essBalMap.put("광운인되기", 1);
                        break;
                    case "대학영어":
                        essBalMap.put(sectionDetail, 3);
                        break;
                    case "정보":
                        // map 컬렉션의 value에 현재 강의의 credit을 더해줌
                        essBalMap.compute(sectionDetail, (s, v) -> v + lecture.getCredit());
                        break;
                    case "융합적사고와글쓰기":
                        // 융사 강의는 학점이 3으로 고정이면서 강의가 1개이므로 그냥 put
                        essBalMap.put(sectionDetail, 3);
                        break;
                    case "과학과기술":
                    case "인간과철학":
                    case "사회와경제":
                    case "글로벌문화와제2외국어":
                        // map 컬렉션의 value에 현재 강의의 credit을 더해줌
                        essBalMap.compute(sectionDetail, (s, v) -> v + lecture.getCredit());
                        break;
                    default:
                        /** 여기에 에러 출력하는 코드 작성해야함 */
                        break;
                }
            }
        }
    }
}
