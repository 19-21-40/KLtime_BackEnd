package com.example.demo;


import com.example.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;


        public void dbInit1(){

            Department department1 = new Department("소프트웨어학부", "소융대");
            Department department2 = new Department("컴퓨터정보공학부", "소융대");

            em.persist(department1);
            em.persist(department2);


            Student student1 = new Student("이성훈", department1, 2, 2019);
            Student student2 = new Student("나부겸", department1, 2, 2019);
            Student student3 = new Student("김수연", department2, 2, 2021);

            em.persist(student1);
            em.persist(student2);
            em.persist(student3);

            GradCondition gradCondition1 = new GradCondition(2019, 133, 22, 21, department1, 60, false);
            GradCondition gradCondition2 = new GradCondition(2019, 133, 22, 21, department1, 54, true);
            gradCondition2.setMultiCredit(54);
            GradCondition gradCondition3 = new GradCondition(2019, 133, 22, 24, department2, 60, false);

            em.persist(gradCondition1);
            em.persist(gradCondition2);
            em.persist(gradCondition3);

            Lecture lecture1 = new Lecture("H000-1-3362-01", "대학영어", "고현아", "교필", "대학영어", 3, 1, "공통", 2019, "2학기");
            Lecture lecture2 = new Lecture("H000-1-3362-02", "대학영어", "브라이언", "교필", "대학영어", 3, 1, "공통", 2022, "2학기");
            Lecture lecture3 = new Lecture("H000-1-3362-03", "대학영어", "김지희", "교필", "대학영어", 3, 1, "공통", 2022, "2학기");

            Lecture lecture4 = new Lecture("7000-1-3095-02", "융합적사고와글쓰기", "전도현", "교필", "융합적사고와글쓰기", 3, 1, "공통", 2022, "2학기");
            Lecture lecture5 = new Lecture("H000-1-3362-03", "융합적사고와글쓰기", "손미영", "교필", "융합적사고와글쓰기", 3, 1, "공통", 2022, "2학기");

            Lecture lecture6 = new Lecture("H030-1-8297-01", "컴퓨팅사고", "손미영", "교필", "정보", 3, 1, "소프트웨어학부", 2019, "2학기");

            Lecture lecture7 = new Lecture("H030-1-5714-01", "고급C프로그래밍및설계", "안우현", "기필", "null", 3, 1, "소프트웨어학부", 2019, "2학기");
            Lecture lecture8 = new Lecture("H030-1-5714-01", "선형대수학", "김상목", "기선", "수학", 3, 2, "소프트웨어학부", 2022, "2학기");

            Lecture lecture9 = new Lecture("0000-1-8583-01", "글로벌시대의쟁점과현안", "전진호", "교선", "사회와경제", 3, 1, "공통", 2019, "2학기");
            Lecture lecture10 = new Lecture("0000-1-5901-01", "기술경영과마케팅", "김찬모", "교선", "사회와경제", 3, 1, "공통", 2022, "2학기");
            Lecture lecture11 = new Lecture("0000-1-0670-01", "법과생활", "손명지", "교선", "사회와경제", 3, 1, "공통", 2022, "2학기");
            Lecture lecture12 = new Lecture("0000-1-0806-01", "생활속의경제", "이수욱", "교선", "사회와경제", 3, 1, "공통", 2022, "2학기");

            Lecture lecture13 = new Lecture("0000-1-3589-01", "독일어1", "조규희", "교선", "글로벌문화와제2외국어", 3, 1, "공통", 2019, "2학기");
            Lecture lecture14 = new Lecture("0000-1-3812-01", "스페인어1", "김찬모", "교선", "글로벌문화와제2외국어", 3, 1, "공통", 2022, "2학기");
            Lecture lecture15 = new Lecture("0000-1-5688-01", "일본문화읽기", "손명지", "교선", "글로벌문화와제2외국어", 3, 1, "공통", 2022, "2학기");
            Lecture lecture16 = new Lecture("0000-1-6524-01", "일본어듣기와쓰기", "이수욱", "교선", "글로벌문화와제2외국어", 3, 1, "공통", 2022, "2학기");

            Lecture lecture17 = new Lecture("H030-2-1243-02", "자료구조", "김용혁", "전필", "null", 3, 2, "소프트웨어학부", 2022, "2학기");
            Lecture lecture18 = new Lecture("H030-2-3395-01", "시스템소프트웨어", "안우현", "전선", "null", 3, 2, "소프트웨어학부", 2022, "2학기");

            Lecture lecture19 = new Lecture("H000-1-3417-01 ", "대학화학및실험2", "최한", "기선", "기초과학", 3, 1, "공통", 2022, "2학기");

            Lecture lecture20 = new Lecture("H030-2-1243-03", "자료구조", "김용혁", "전필", "null", 3, 2, "소프트웨어학부", 2022, "2학기");
            Lecture lecture21 = new Lecture("H030-2-3405-02", "자료구조실습", "김용혁", "전필", "null", 2, 2, "소프트웨어학부", 2022, "2학기");
            Lecture lecture22 = new Lecture("H030-2-7777-02", "객체지향프로그래밍", "김진우", "전선", "null", 3, 2, "소프트웨어학부", 2022, "2학기");
            Lecture lecture23 = new Lecture("H030-2-8487-01", "오픈소스소프트웨어개발", "문승현", "전선", "null", 3, 2, "소프트웨어학부", 2022, "2학기");


            persistLectures(lecture1, lecture2, lecture3, lecture4, lecture5, lecture6, lecture7, lecture8);
            persistLectures(lecture9, lecture10, lecture11, lecture12, lecture13, lecture14, lecture15, lecture16);
            persistLectures(lecture17, lecture18, lecture19, lecture20, lecture21, lecture22, lecture23);

            student1.addLectureToStudent(lecture1, "A0", 1,2);
            student1.addLectureToStudent(lecture4, "A+", 1,2);
            student1.addLectureToStudent(lecture6, "A0", 1, 2);
            student1.addLectureToStudent(lecture7, "A+", 1,2);
            student1.addLectureToStudent(lecture9, "B+", 1, 1);
            student1.addLectureToStudent(lecture10, "B0", 2, 2);
            student1.addLectureToStudent(lecture11, "F", 2,2);
            student1.addLectureToStudent(lecture13,"C+",1,2);
            student1.addLectureToStudent(lecture17,"B+",2,2);
            student1.addLectureToStudent(lecture18,"A0",2,2);
            student1.addLectureToStudent(lecture19,"A0",2,2);

        }

        private void persistLectures(Lecture... lectures) {
            for (Lecture lecture : lectures) {
                em.persist(lecture);
            }
        }

    }
}
