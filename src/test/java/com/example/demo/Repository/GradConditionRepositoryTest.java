//package com.example.demo.Repository;
//
//import com.example.demo.domain.Department;
//import com.example.demo.domain.GradCondition;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//public class GradConditionRepositoryTest {
//
//    @Autowired GradConditionRepository gradConditionRepository;
//    @Autowired DepartmentRepository departmentRepository;
//    @Autowired EntityManager em;
//
//    @Test
//    @Rollback(false)
//    public void 졸업조건기입() throws Exception {
//        //given
//        Department dept1 = new Department("소프트웨어학부", "소융대");
//        Department dept2 = new Department("컴퓨터정보공학부", "소융대");
//
//        GradCondition grad1 = new GradCondition(2022, 85, 35, 30, dept1, 20, false);
//        GradCondition grad2 = new GradCondition(2022, 75, 20, 45, dept2, 10, false);
//
//        //when
//        Long a1 = departmentRepository.save(dept1);
//        Long a2 = departmentRepository.save(dept2);
//
//        Long b1 = gradConditionRepository.save(grad1);
//        Long b2 = gradConditionRepository.save(grad2);
//
//        //then
//        em.flush();
////        GradCondition result = gradConditionRepository.findByDeptAndAdmissionYear(dept1, 2022);
//
////        System.out.println(result.getId());
//    }
//
//}