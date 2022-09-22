package com.example.demo.Service;

import com.example.demo.Repository.*;
import com.example.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Collectors.*;


import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendLectureService {

    private final StudentRepository studentRepository;
    private final GradConditionRepository gradConditionRepository;
    private final LectureRepository lectureRepository;
    private final DepartmentRepository departmentRepository;


    /**
     * 학생의 수강과목을 조회하여 student.Credit 객체에 저장함
     * @Param studentId
     * @Return void
     * */
    public void checkAndSaveCredit(Long studentId) {

        Student student = studentRepository.findByIdWithLecture(studentId);

        // 학생의 학번과 학과 불러오기 // 임의로 데이터베이스에 저장된 Id=3인 "이성훈"을 불러옴
        Department department = departmentRepository.findById(student.getDepartment().getId());

        // 그에 맞는 졸업 학점 조건 불러오기
        GradCondition gradCondition;

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
            if (sl.getGpa().equals("F") || sl.getGpa().equals("NP")) continue;

            // 그게 아니라면
            // totalCredit에 현재강의의 학점을 더해줌
            temporalCredit.addTotalCredit(lecture.getCredit());


            /** 19학번 기준 */
            // 교필과 교선에 대해서 먼저 따져줌 ( sectionDetail에 따라 처리해줘야 함 )
            if (section.equals("교필") || section.equals("교선")) {
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
                        if (student.getDepartment().getName().equals("경영학부") || student.getDepartment().getName().equals("국제통상학부") ||
                                student.getDepartment().getCollegeName().equals("자연대") || student.getDepartment().getCollegeName().equals("소융대"))
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

    /**
     * 전공 과목들에 대해서 (중복 강의 없이) 출력함
     * @Param studentId
     * @Return List<Lecture>
     * */
    public List<Lecture> recommendMainLectureWithNoDup(Long studentId) {

        // 학생의 정보들을 불러옴
        Student student = studentRepository.findByIdWithLecture(studentId);

        // result에 전필과 전선 과목들을 불러옴
        List<Lecture> result = lectureRepository.findMainLecturesByTwoSection(student.getDepartment());

        /** 중복 제거를 위한 과정 */
        // 중복되는 이름을 필터링하기 위해서 HashSet 컬렉션타입을 선언
        Set<String> resultSet = new HashSet<>();
        // result에서 강의명이 같은 강의들을 삭제함(HashSet에 기존 값이 있을 경우 add가 안돼서 false를 반환하기 때문)
        result.removeIf(lecture -> !resultSet.add(lecture.getName()));


        /** 학생이 들었던 강의를 결과에서 지우기 위한 과정 */
        // 학생이 들은 강의들 중 "전필"과 "전선" 과목으로 필터링함.
        List<StudentLecture> myLectures = student.getMyLectures().stream()
                .filter( l -> l.getLecture().getSection().equals("전선")||l.getLecture().getSection().equals("전필"))
                .collect(Collectors.toList());

        for (StudentLecture sl : myLectures) {

            // F이거나 NP이면 강의 목록에서 삭제하지 않음.
            if(sl.getGpa() == "F" || sl.getGpa() == "NP") continue;

            String lectureName = sl.getLecture().getName();

            // result에 있는 강의명 중에 학생이 들은 강의가 존재한다면 result에서 해당 강의를 삭제함.
            result.removeIf(lecture -> lecture.getName().equals(lectureName));
        }

        System.out.println(result);

        return result;

    }


    /**
     * 기초교양 과목들에 대해서 (중복 강의 없이) 출력함
     * @Param StudentId
     * @Return List<Lecture>
     * */
    public List<Lecture> recommendBasicLectureWithNoDup(Long studentId) {

        // 학생의 정보들을 불러옴
        Student student = studentRepository.findByIdWithLecture(studentId);

        // result에 기초교양 과목들을 불러옴
        List<Lecture> result = lectureRepository.findByTwoSection("기필", "기선");


        /** 중복 제거를 위한 과정 */
        // 중복되는 이름을 필터링하기 위해서 HashSet 컬렉션타입을 선언
        Set<String> resultSet = new HashSet<>();
        // result에서 강의명이 같은 강의들을 삭제함(HashSet에 기존 값이 있을 경우 add가 안돼서 false를 반환하기 때문)
        result.removeIf(lecture -> !resultSet.add(lecture.getName()));



        /** 학생이 들었던 강의를 결과에서 지우기 위한 과정 */
        // 학생이 들은 강의들 중 "기필"과 "기선" 과목으로 필터링함.
        List<StudentLecture> myLectures = student.getMyLectures().stream()
                .filter( l -> l.getLecture().getSection().equals("기필")||l.getLecture().getSection().equals("기선"))
                .collect(Collectors.toList());

        for (StudentLecture sl : myLectures) {

            // F이거나 NP이면 강의 목록에서 삭제하지 않음.
            if(sl.getGpa() == "F" || sl.getGpa() == "NP") continue;

            String lectureName = sl.getLecture().getName();

            // result에 있는 강의명 중에 학생이 들은 강의가 존재한다면 result에서 해당 강의를 삭제함.
            result.removeIf(lecture -> lecture.getName().equals(lectureName));
        }

        System.out.println(result);

        return result;
    }

    public List<Lecture> recommendBasicScienceLectureWithNoDup(Long studentId) {
        // 학생의 정보들을 불러옴
        Student student = studentRepository.findByIdWithLecture(studentId);

        // result에 기초교양 과목들을 불러옴
        List<Lecture> result = lectureRepository.findBySectionDetail("기초과학");


        /** 중복 제거를 위한 과정 */
        // 중복되는 이름을 필터링하기 위해서 HashSet 컬렉션타입을 선언
        Set<String> resultSet = new HashSet<>();
        // result에서 강의명이 같은 강의들을 삭제함(HashSet에 기존 값이 있을 경우 add가 안돼서 false를 반환하기 때문)
        result.removeIf(lecture -> !resultSet.add(lecture.getName()));



        /** 학생이 들었던 강의를 결과에서 지우기 위한 과정 */
        // 학생이 들은 강의들 중 "기필"과 "기선" 과목으로 필터링함.
        List<StudentLecture> myLectures = student.getMyLectures().stream()
                .filter( l -> l.getLecture().getSection().equals("기필")||l.getLecture().getSection().equals("기선"))
                .collect(Collectors.toList());

        for (StudentLecture sl : myLectures) {

            // F이거나 NP이면 강의 목록에서 삭제하지 않음.
            if(sl.getGpa() == "F" || sl.getGpa() == "NP") continue;

            String lectureName = sl.getLecture().getName();

            // result에 있는 강의명 중에 학생이 들은 강의가 존재한다면 result에서 해당 강의를 삭제함.
            result.removeIf(lecture -> lecture.getName().equals(lectureName));
        }

        System.out.println(result);

        return result;
    }

    public List<Lecture> recommendMathLectureWithNoDup(Long studentId) {
        // 학생의 정보들을 불러옴
        Student student = studentRepository.findByIdWithLecture(studentId);

        // result에 기초교양 과목들을 불러옴
        List<Lecture> result = lectureRepository.findBySectionDetail("수학");


        /** 중복 제거를 위한 과정 */
        // 중복되는 이름을 필터링하기 위해서 HashSet 컬렉션타입을 선언
        Set<String> resultSet = new HashSet<>();
        // result에서 강의명이 같은 강의들을 삭제함(HashSet에 기존 값이 있을 경우 add가 안돼서 false를 반환하기 때문)
        result.removeIf(lecture -> !resultSet.add(lecture.getName()));


        /** 학생이 들었던 강의를 결과에서 지우기 위한 과정 */
        // 학생이 들은 강의들 중 "기필"과 "기선" 과목으로 필터링함.
        List<StudentLecture> myLectures = student.getMyLectures().stream()
                .filter( l -> l.getLecture().getSection().equals("기필")||l.getLecture().getSection().equals("기선"))
                .collect(Collectors.toList());

        for (StudentLecture sl : myLectures) {

            // F이거나 NP이면 강의 목록에서 삭제하지 않음.
            if(sl.getGpa() == "F" || sl.getGpa() == "NP") continue;

            String lectureName = sl.getLecture().getName();

            // result에 있는 강의명 중에 학생이 들은 강의가 존재한다면 result에서 해당 강의를 삭제함.
            result.removeIf(lecture -> lecture.getName().equals(lectureName));
        }

        System.out.println(result);

        return result;
    }


    /**
     * 균형교양 과목들에 대해서 sectionDetail로 나눠서 (중복 강의 없이) 출력함
     * @Param StudentId
     * @Return Map<String, List>
     * */
    public Map<String, List<Lecture>> recommendOnlyBalLecturesWithNoDup(Long studentId) {

        // 학생의 정보를 불러옴
        Student student = studentRepository.findByIdWithLecture(studentId);

        Map<String, Integer> bal_Lec_Map = new HashMap<>();

        bal_Lec_Map.put("융합적사고와글쓰기", 0);
        bal_Lec_Map.put("과학과기술", 0);
        bal_Lec_Map.put("인간과철학", 0);
        bal_Lec_Map.put("사회와경제", 0);
        bal_Lec_Map.put("글로벌문화와제2외국어", 0);
        bal_Lec_Map.put("예술과체육", 0);

        // 학생이 들었던 과목 리스트
        List<StudentLecture> myLectures = student.getMyLectures();

        // 내가 들었던 강의들에 대해 for문을 돌림
        for (StudentLecture sl : myLectures) {

            // 코드를 짧게 작성하기 위해 따로 초기화해줬음
            Lecture lecture = sl.getLecture();
            String section = lecture.getSection();
            String sectionDetail = lecture.getSectionDetail();

            // 받은 학점이 F or NP라면 continue 실행
            if(sl.getGpa().equals("F")  || sl.getGpa().equals("NP")) continue;

            /** 19학번 기준 */
            // 교필과 교선에 대해서 먼저 따져줌 ( sectionDetail에 따라 처리해줘야 함 )
            if(section.equals("교필") || section.equals("교선")) {
                switch (sectionDetail) {
                    case "융합적사고와글쓰기":
                        // 단과대학이나 학과에 따라 균형교양, 필수교양으로 갈림. 그에 따라 코드 작성했음
                        // 융사 강의는 학점이 3으로 고정이면서 강의가 1개이므로 3학점을 put
                        if(student.getDepartment().getName().equals("경영학부") || student.getDepartment().getName().equals("국제통상학부") ||
                                student.getDepartment().getCollegeName().equals("자연대") || student.getDepartment().getCollegeName().equals("소융대"))
                            bal_Lec_Map.put(sectionDetail, 3);
                        break;
                    case "과학과기술":
                    case "인간과철학":
                    case "사회와경제":
                    case "글로벌문화와제2외국어":
                    case "예술과체육":
                        // map 컬렉션의 value에 현재 강의의 credit을 더해줌
                        bal_Lec_Map.compute(sectionDetail, (s, v) -> v + lecture.getCredit());
                        break;
                    default:
                        /** 여기에 에러 출력하는 코드 작성해야함 */
                        break;
                }
            }
        }

        // 충족한 sectionDetail의 수
        int fulfilled_count = 0;

        Set<String> needs_bal = new HashSet<>();

        if(student.getDepartment().getName().equals("경영학부") || student.getDepartment().getName().equals("국제통상학부") ||
                student.getDepartment().getCollegeName().equals("자연대") || student.getDepartment().getCollegeName().equals("소융대")){
            if(bal_Lec_Map.get("융합적사고와글쓰기") < 3){
                needs_bal.add("융합적사고와글쓰기");
            }
            else{
                fulfilled_count++;
            }
        }

        // 균형교양 학점이 3보다 낮다면, Map 변수 needs_bal<section, 부족한 학점>을 대입한다.
        for (String sectionDetail : bal_Lec_Map.keySet()) {
            if ( sectionDetail == "융합적사고와글쓰기") continue;
            if (bal_Lec_Map.get(sectionDetail) < 3) {
                needs_bal.add(sectionDetail);
            } else {
                // 충족했다면 "만족한 균형교양의 수 ++" 를 실행
                fulfilled_count++;
            }
        }

        // "만족한 균형교양의 수" 가 4 이상이라면 ( 19학번 소융대 기준 ) needs_bal을 다 비운다. 그 이유는 다음줄
        if(fulfilled_count >= 4){
            needs_bal.clear();
        }



        Map<String, List<Lecture>> lectureListMappedSectionDetail = new HashMap<>();


        if (needs_bal.isEmpty() == false) {

            List<Lecture> balLectureList = lectureRepository.findBySectionDetailSet(needs_bal);

            System.out.println("이름이 같은 강의 중복 제거 전");
            System.out.println(balLectureList);

            /** 중복 제거를 위한 과정 */
            // 중복되는 이름을 필터링하기 위해서 HashSet 컬렉션타입을 선언
            Set<String> resultSet = new HashSet<>();
            // result에서 강의명이 같은 강의들을 삭제함(HashSet에 기존 값이 있을 경우 add가 안돼서 false를 반환하기 때문)
            balLectureList.removeIf(lecture -> !resultSet.add(lecture.getName()));

            System.out.println("이름이 같은 강의 중복 제거 후");
            System.out.println(balLectureList);


            /** 학생이 들었던 강의를 결과에서 지우기 위한 과정 */
            // 학생이 들은 강의들 중 "교필"과 "교선" 과목으로 필터링함.
            myLectures = student.getMyLectures().stream()
                    .filter( l -> l.getLecture().getSection().equals("교필")||l.getLecture().getSection().equals("교선"))
                    .collect(Collectors.toList());


            for (StudentLecture sl : myLectures) {

                // F이거나 NP이면 강의 목록에서 삭제하지 않음.
                if(sl.getGpa() == "F" || sl.getGpa() == "NP") continue;

                String lectureName = sl.getLecture().getName();

                // result에 있는 강의명 중에 학생이 들은 강의가 존재한다면 result에서 해당 강의를 삭제함.
                balLectureList.removeIf(lecture -> lecture.getName().equals(lectureName));
            }

            System.out.println("학생이 들었던 과목 제외 후");
            System.out.println(balLectureList);

            /** 학생에게 필요한 section들에 따른 과목들을 Map<String, List<Lecture>>에 매핑함 */
            for (String sectionDetail : needs_bal) {
                System.out.println("sectionDetail " + sectionDetail + "이 필요합니다.");

                List<Lecture> lectureListBySection = balLectureList.stream()
                        .filter(lecture -> lecture.getSectionDetail().equals(sectionDetail))
                                .collect(Collectors.toList());
                System.out.println(sectionDetail +"의 과목리스트 : " + lectureListBySection);
                lectureListMappedSectionDetail.put(sectionDetail, lectureListBySection);
            }
        }


        return lectureListMappedSectionDetail;
    }


    /**
     * 필수교양 과목들에 대해서 sectionDetail로 나눠서 (중복 강의 없이) 출력함
     * @Param StudentId
     * @Return Map<String, List>
     * */
    public Map<String, List<Lecture>> recommendOnlyEssLecturesWithNoDup(Long studentId) {

        // 학생의 정보를 불러옴
        Student student = studentRepository.findByIdWithLecture(studentId);

        /** section과 sectionDetail에 따라 학점을 각각 관리하는 코드 */
        // 필수에 대한 학생의 이수학점을 기록할 변수
        Map<String, Integer> ess_Lec_Map = new HashMap<>();

        ess_Lec_Map.put("광운인되기", 0);
        ess_Lec_Map.put("대학영어", 0);
        ess_Lec_Map.put("정보", 0);
        ess_Lec_Map.put("융합적사고와글쓰기", 0);

        // 학생이 들었던 과목 리스트
        List<StudentLecture> myLectures = student.getMyLectures();

        // 내가 들었던 강의들에 대해 for문을 돌림
        for (StudentLecture sl : myLectures) {

            // 코드를 짧게 작성하기 위해 따로 초기화해줬음
            Lecture lecture = sl.getLecture();
            String section = lecture.getSection();
            String sectionDetail = lecture.getSectionDetail();

            // 받은 학점이 F or NP라면 continue 실행
            if(sl.getGpa().equals("F")  || sl.getGpa().equals("NP")) continue;

            /** 19학번 기준 */
            // 교필과 교선에 대해서 먼저 따져줌 ( sectionDetail에 따라 처리해줘야 함 )
            if(section.equals("교필") || section.equals("교선")) {
                switch (sectionDetail) {
                    case "광운인되기":
                        // 광운인되기 강의는 학점이 1으로 고정이면서 강의가 1개이므로 그냥 put
                        ess_Lec_Map.put("광운인되기", 1);
                        break;
                    case "대학영어":
                        ess_Lec_Map.put(sectionDetail, 3);
                        break;
                    case "정보":
                        // map 컬렉션의 value에 현재 강의의 credit을 더해줌
                        ess_Lec_Map.compute(sectionDetail, (s, v) -> v + lecture.getCredit());
                        break;
                    case "융합적사고와글쓰기":
                        // 단과대학이나 학과에 따라 균형교양, 필수교양으로 갈림. 그에 따라 코드 작성했음
                        // 융사 강의는 학점이 3으로 고정이면서 강의가 1개이므로 3학점을 put
                        if(student.getDepartment().getName().equals("경영학부") || student.getDepartment().getName().equals("국제통상학부") ||
                                student.getDepartment().getCollegeName().equals("자연대") || student.getDepartment().getCollegeName().equals("소융대"))
                            break;
                        else {
                            ess_Lec_Map.put(sectionDetail, 3);
                        }
                        break;
                    default:
                        /** 여기에 에러 출력하는 코드 작성해야함 */
                        break;
                }
            }
        }

        Set<String> needs = new HashSet<>();

        if(ess_Lec_Map.get("광운인되기") < 1)
            needs.add("광운인되기");
        if(ess_Lec_Map.get("대학영어") < 3)
            needs.add("대학영어");
        if(ess_Lec_Map.get("정보") < 6)
            needs.add("정보");

        // not 연산 붙였음 (주의)
        if(!(student.getDepartment().getName().equals("경영학부") || student.getDepartment().getName().equals("국제통상학부") ||
                student.getDepartment().getCollegeName().equals("자연대") || student.getDepartment().getCollegeName().equals("소융대"))) {
            if (ess_Lec_Map.get("융합적사고와글쓰기") < 3)
                needs.add("융합적사고와글쓰기");
        }

        Map<String, List<Lecture>> lectureListMappedSectionDetail = new HashMap<>();


        if (needs.isEmpty() == false) {

            List<Lecture> balLectureList = lectureRepository.findBySectionDetailSet(needs);

            System.out.println("이름이 같은 강의 중복 제거 전");
            System.out.println(balLectureList);

            /** 중복 제거를 위한 과정 */
            // 중복되는 이름을 필터링하기 위해서 HashSet 컬렉션타입을 선언
            Set<String> resultSet = new HashSet<>();
            // result에서 강의명이 같은 강의들을 삭제함(HashSet에 기존 값이 있을 경우 add가 안돼서 false를 반환하기 때문)
            balLectureList.removeIf(lecture -> !resultSet.add(lecture.getName()));

            System.out.println("이름이 같은 강의 중복 제거 후");
            System.out.println(balLectureList);


            /** 학생이 들었던 강의를 결과에서 지우기 위한 과정 */
            // 학생이 들은 강의들 중 "교필"과 "교선" 과목으로 필터링함.
            myLectures = student.getMyLectures().stream()
                    .filter( l -> l.getLecture().getSection().equals("교필")||l.getLecture().getSection().equals("교선"))
                    .collect(Collectors.toList());


            for (StudentLecture sl : myLectures) {

                // F이거나 NP이면 강의 목록에서 삭제하지 않음.
                if(sl.getGpa() == "F" || sl.getGpa() == "NP") continue;

                String lectureName = sl.getLecture().getName();

                // result에 있는 강의명 중에 학생이 들은 강의가 존재한다면 result에서 해당 강의를 삭제함.
                balLectureList.removeIf(lecture -> lecture.getName().equals(lectureName));
            }

            System.out.println("학생이 들었던 과목 제외 후");
            System.out.println(balLectureList);

            /** 학생에게 필요한 section들에 따른 과목들을 Map<String, List<Lecture>>에 매핑함 */
            for (String sectionDetail : needs) {
                System.out.println("sectionDetail " + sectionDetail + "이 필요합니다.");

                List<Lecture> lectureListBySection = balLectureList.stream()
                        .filter(lecture -> lecture.getSectionDetail().equals(sectionDetail))
                        .collect(Collectors.toList());
                System.out.println(sectionDetail +"의 과목리스트 : " + lectureListBySection);
                lectureListMappedSectionDetail.put(sectionDetail, lectureListBySection);
            }
        }

        return lectureListMappedSectionDetail;
    }


    /**
     * 필수교양+균형교양 과목들에 대해서 (중복 강의 없이) 출력함
     * @Param StudentId
     * @Return Map<String, List>
     * */
    public Map<String, List<Lecture>> recommendEssBalLecturesWithNoDup(Long studentId) {

        // 학생의 정보를 불러옴
        Student student = studentRepository.findByIdWithLecture(studentId);

        /** section과 sectionDetail에 따라 학점을 각각 관리하는 코드 */
        // 필수에 대한 학생의 이수학점을 기록할 변수
        Map<String, Integer> ess_Lec_Map = new HashMap<>();

        ess_Lec_Map.put("광운인되기", 0);
        ess_Lec_Map.put("대학영어", 0);
        ess_Lec_Map.put("정보", 0);
        ess_Lec_Map.put("융합적사고와글쓰기", 0);

        // 학생이 들었던 과목 리스트
        List<StudentLecture> myLectures = student.getMyLectures();

        // 내가 들었던 강의들에 대해 for문을 돌림
        for (StudentLecture sl : myLectures) {

            // 코드를 짧게 작성하기 위해 따로 초기화해줬음
            Lecture lecture = sl.getLecture();
            String section = lecture.getSection();
            String sectionDetail = lecture.getSectionDetail();

            // 받은 학점이 F or NP라면 continue 실행
            if(sl.getGpa().equals("F")  || sl.getGpa().equals("NP")) continue;

            /** 19학번 기준 */
            // 교필과 교선에 대해서 먼저 따져줌 ( sectionDetail에 따라 처리해줘야 함 )
            if(section.equals("교필") || section.equals("교선")) {
                switch (sectionDetail) {
                    case "광운인되기":
                        // 광운인되기 강의는 학점이 1으로 고정이면서 강의가 1개이므로 그냥 put
                        ess_Lec_Map.put("광운인되기", 1);
                        break;
                    case "대학영어":
                        ess_Lec_Map.put(sectionDetail, 3);
                        break;
                    case "정보":
                        // map 컬렉션의 value에 현재 강의의 credit을 더해줌
                        ess_Lec_Map.compute(sectionDetail, (s, v) -> v + lecture.getCredit());
                        break;
                    case "융합적사고와글쓰기":
                        // 단과대학이나 학과에 따라 균형교양, 필수교양으로 갈림. 그에 따라 코드 작성했음
                        // 융사 강의는 학점이 3으로 고정이면서 강의가 1개이므로 3학점을 put
                        if(student.getDepartment().getName().equals("경영학부") || student.getDepartment().getName().equals("국제통상학부") ||
                                student.getDepartment().getCollegeName().equals("자연대") || student.getDepartment().getCollegeName().equals("소융대"))
                            break;
                        else {
                            ess_Lec_Map.put(sectionDetail, 3);
                        }
                        break;
                    default:
                        /** 여기에 에러 출력하는 코드 작성해야함 */
                        break;
                }
            }
        }

        Set<String> needs = new HashSet<>();

        // 필수교양 과목에 대해서는 일일이 계산 진행
        if(ess_Lec_Map.get("광운인되기") < 1)
            needs.add("광운인되기");
        if(ess_Lec_Map.get("대학영어") < 3)
            needs.add("대학영어");
        if(ess_Lec_Map.get("정보") < 6)
            needs.add("정보");

        // not 연산 붙였음 (주의)
        if(!(student.getDepartment().getName().equals("경영학부") || student.getDepartment().getName().equals("국제통상학부") ||
                student.getDepartment().getCollegeName().equals("자연대") || student.getDepartment().getCollegeName().equals("소융대"))) {
            if (ess_Lec_Map.get("융합적사고와글쓰기") < 3)
                needs.add("융합적사고와글쓰기");
        }

        needs.add("인간과철학");
        needs.add("사회와경제");
        needs.add("글로벌문화와제2외국어");
        needs.add("예술과체육");
        needs.add("과학과기술");


        Map<String, List<Lecture>> lectureListMappedSectionDetail = new HashMap<>();


        if (needs.isEmpty() == false) {

            List<Lecture> essBalLectureList = lectureRepository.findBySectionDetailSet(needs);


            /** 중복 제거를 위한 과정 */
            // 중복되는 이름을 필터링하기 위해서 HashSet 컬렉션타입을 선언
            Set<String> resultSet = new HashSet<>();
            // result에서 강의명이 같은 강의들을 삭제함(HashSet에 기존 값이 있을 경우 add가 안돼서 false를 반환하기 때문)
            essBalLectureList.removeIf(lecture -> !resultSet.add(lecture.getName()));


            /** 학생이 들었던 강의를 결과에서 지우기 위한 과정 */
            // 학생이 들은 강의들 중 "교필"과 "교선" 과목으로 필터링함.
            myLectures = student.getMyLectures().stream()
                    .filter( l -> l.getLecture().getSection().equals("교필")||l.getLecture().getSection().equals("교선"))
                    .collect(Collectors.toList());

            for (StudentLecture sl : myLectures) {

                // F이거나 NP이면 강의 목록에서 삭제하지 않음.
                if(sl.getGpa() == "F" || sl.getGpa() == "NP") continue;

                String lectureName = sl.getLecture().getName();

                // result에 있는 강의명 중에 학생이 들은 강의가 존재한다면 result에서 해당 강의를 삭제함.
                essBalLectureList.removeIf(lecture -> lecture.getName().equals(lectureName));
            }

            /** 학생에게 필요한 section들에 따른 과목들을 Map<String, List<Lecture>>에 매핑함 */
            for (String sectionDetail : needs) {
                System.out.println("sectionDetail " + sectionDetail + "이 필요합니다.");

                List<Lecture> lectureListBySection = essBalLectureList.stream()
                        .filter(lecture -> lecture.getSectionDetail().equals(sectionDetail))
                        .collect(Collectors.toList());
                System.out.println(sectionDetail +"의 과목리스트 : " + lectureListBySection);
                lectureListMappedSectionDetail.put(sectionDetail, lectureListBySection);
            }
        }

        return lectureListMappedSectionDetail;
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


    /**
     * (학과 및 학번에 따라 다름) 학생의 필수과목들중에서 학생이 수강한 과목들을 배제하고 출력함
     * @Param Student
     * @Return Set<String> req_lec (남아있는 필수 과목들)
     * */
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


}
