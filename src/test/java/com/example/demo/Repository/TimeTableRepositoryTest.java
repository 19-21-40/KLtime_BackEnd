package com.example.demo.Repository;

import com.example.demo.domain.Lecture;
import com.example.demo.domain.Student;
import com.example.demo.domain.TimeTable;
import com.example.demo.domain.TimeTableLecture;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class TimeTableRepositoryTest {

    @Autowired
    TimeTableRepository timeTableRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    EntityManager em;

    @Test
    @Rollback(false)
    public void save() throws Exception {
        //given
        Student student1 = new Student();
        Lecture lecture=Lecture.createLecture(
                "H030-02-0819-01",
                "선형대수학",
                "김상목",
                "기선",
                "수학",
                3,
                2,
                "소프트웨어학부",
                2022,
                "1학기",
                "");
        TimeTableLecture timeTableLecture1 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable1 = TimeTable.createTimetable(student1,"시간표1",1,1,true,timeTableLecture1);
        //when
        em.persist(student1);
        timeTableRepository.save(timeTable1);
        //then
    }

    @Test
    @Rollback(false)
    public void findOne() throws Exception {
        //given
        Student student1 = new Student();
        Lecture lecture=Lecture.createLecture(
                "H030-02-0819-01",
                "선형대수학",
                "김상목",
                "기선",
                "수학",
                3,
                2,
                "소프트웨어학부",
                2022,
                "1학기",
                "");
        TimeTableLecture timeTableLecture1 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable1 = TimeTable.createTimetable(student1,"시간표1",1,1,true,timeTableLecture1);

        Student student2 = new Student();
        TimeTableLecture timeTableLecture2 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable2 = TimeTable.createTimetable(student2,"시간표2",2,2,false,timeTableLecture2);
        //when
        em.persist(student1);
        em.persist(student2);
        timeTableRepository.save(timeTable1);
        timeTableRepository.save(timeTable2);
        //then
        assertEquals(timeTable1,timeTableRepository.findOne(timeTable1.getId()));
    }

    @Test
    @Rollback(false)
    public void findByStudent() throws Exception{
        //given
        Student student1 = new Student();
        Lecture lecture=Lecture.createLecture(
                "H030-02-0819-01",
                "선형대수학",
                "김상목",
                "기선",
                "수학",
                3,
                2,
                "소프트웨어학부",
                2022,
                "1학기",
                "");
        TimeTableLecture timeTableLecture1 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable1 = TimeTable.createTimetable(student1,"시간표1",1,1,true,timeTableLecture1);

        Student student2 = new Student();
        TimeTableLecture timeTableLecture2 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable2 = TimeTable.createTimetable(student2,"시간표2",2,2,false,timeTableLecture2);

        TimeTableLecture timeTableLecture3 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable3 = TimeTable.createTimetable(student1, "시간표2", 1, 2, false, timeTableLecture3);
        //when
        em.persist(student1);
        em.persist(student2);
        timeTableRepository.save(timeTable1);
        timeTableRepository.save(timeTable2);
        timeTableRepository.save(timeTable3);
        //then
        assertEquals(2,timeTableRepository.findByStudent(student1).size());
    }

    @Test
    @Rollback(false)
    public void findByStudentAndGrade() throws Exception {
        //given
        Student student1 = new Student();
        Lecture lecture=Lecture.createLecture(
                "H030-02-0819-01",
                "선형대수학",
                "김상목",
                "기선",
                "수학",
                3,
                2,
                "소프트웨어학부",
                2022,
                "1학기",
                "");
        TimeTableLecture timeTableLecture1 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable1 = TimeTable.createTimetable(student1,"시간표1",1,1,true,timeTableLecture1);

        Student student2 = new Student();
        TimeTableLecture timeTableLecture2 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable2 = TimeTable.createTimetable(student2,"시간표2",2,2,false,timeTableLecture2);

        TimeTableLecture timeTableLecture3 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable3 = TimeTable.createTimetable(student1, "시간표2", 1, 2, false, timeTableLecture3);
        //when
        em.persist(student1);
        em.persist(student2);
        timeTableRepository.save(timeTable1);
        timeTableRepository.save(timeTable2);
        timeTableRepository.save(timeTable3);
        //then
        assertEquals(2,timeTableRepository.findByStudentAndGrade(student1,1).size());
    }

    @Test
    @Rollback(false)
    public void findByStudentAndGradeAndSemester() throws Exception {
        //given
        Student student1 = new Student();
        Lecture lecture=Lecture.createLecture(
                "H030-02-0819-01",
                "선형대수학",
                "김상목",
                "기선",
                "수학",
                3,
                2,
                "소프트웨어학부",
                2022,
                "1학기",
                "");
        TimeTableLecture timeTableLecture1 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable1 = TimeTable.createTimetable(student1,"시간표1",1,2,true,timeTableLecture1);

        Student student2 = new Student();
        TimeTableLecture timeTableLecture2 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable2 = TimeTable.createTimetable(student2,"시간표2",2,2,false,timeTableLecture2);

        TimeTableLecture timeTableLecture3 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable3 = TimeTable.createTimetable(student1, "시간표2", 1, 2, false, timeTableLecture3);

        TimeTableLecture timeTableLecture4 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable4 = TimeTable.createTimetable(student2, "시간표1", 3, 2, true, timeTableLecture4);
        //when
        em.persist(student1);
        em.persist(student2);
        timeTableRepository.save(timeTable1);
        timeTableRepository.save(timeTable2);
        timeTableRepository.save(timeTable3);
        timeTableRepository.save(timeTable4);
        //then
        assertEquals(2,timeTableRepository.findByStudentAndGradeAndSemester(student1,1,2).size());
    }

    @Test
    @Rollback(false)
    public void findByStudentAndGradeAndSemesterAndPrimary() throws Exception {
        //given
        Student student1 = new Student();
        Lecture lecture=Lecture.createLecture(
                "H030-02-0819-01",
                "선형대수학",
                "김상목",
                "기선",
                "수학",
                3,
                2,
                "소프트웨어학부",
                2022,
                "1학기",
                "");
        TimeTableLecture timeTableLecture1 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable1 = TimeTable.createTimetable(student1,"시간표1",1,2,true,timeTableLecture1);

        Student student2 = new Student();
        TimeTableLecture timeTableLecture2 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable2 = TimeTable.createTimetable(student2,"시간표2",2,2,false,timeTableLecture2);

        TimeTableLecture timeTableLecture3 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable3 = TimeTable.createTimetable(student1, "시간표2", 1, 2, false, timeTableLecture3);

        TimeTableLecture timeTableLecture4 = TimeTableLecture.createTimeTableLecture(lecture);
        TimeTable timeTable4 = TimeTable.createTimetable(student2, "시간표1", 3, 2, true, timeTableLecture4);
        //when
        em.persist(student1);
        em.persist(student2);
        timeTableRepository.save(timeTable1);
        timeTableRepository.save(timeTable2);
        timeTableRepository.save(timeTable3);
        timeTableRepository.save(timeTable4);
        //then
        assertEquals(timeTable1,timeTableRepository.findByStudentAndGradeAndSemesterAndPrimary(student1,1,2,true));
    }
}