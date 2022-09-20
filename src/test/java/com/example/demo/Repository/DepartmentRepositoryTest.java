package com.example.demo.Repository;

import com.example.demo.domain.Department;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class DepartmentRepositoryTest {

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    @Rollback(false)
    public void save() throws Exception{
        //given
        Department department1 = new Department();
        department1.setName("소프트웨어학부");
        department1.setCollegeName("소프트웨어융합대학");
        //when
        departmentRepository.save(department1);
        //then
    }

    //서비스 테스트에서 로직이 있어야 정상적으로 작동하는 듯
//    @Test
//    @Rollback(false)
//    public void 중복_학과_저장() throws Exception{
//        //given
//        Department department1 = new Department();
//        department1.setName("소프트웨어학부");
//        department1.setCollegeName("소프트웨어융합대학");
//
//        Department department2 = new Department();
//        department2.setName("소프트웨어학부");
//        department2.setCollegeName("소프트웨어융합대학");
//        //when
//        departmentRepository.save(department1);
//        IllegalStateException e = assertThrows(IllegalStateException.class,()-> departmentRepository.save(department2) );
//        //then
//        assertEquals("이미 존재하는 학과(부)입니다.", e.getMessage());
//    }

    @Test
    @Rollback(false)
    public void findOne() throws  Exception{
        //given
        Department department1 = new Department();
        department1.setName("소프트웨어학부");
        department1.setCollegeName("소프트웨어융합대학");

        Department department2 = new Department();
        department2.setName("법학부");
        department2.setCollegeName("정책법학대학");
        //when
        departmentRepository.save(department1);
        departmentRepository.save(department2);
        //then
        assertEquals(department1,departmentRepository.findOne(department1.getId()));
    }

    @Test
    @Rollback(false)
    public void findAll() throws Exception{
        //given
        Department department1 = new Department();
        department1.setName("소프트웨어학부");
        department1.setCollegeName("소프트웨어융합대학");

        Department department2 = new Department();
        department2.setName("법학부");
        department2.setCollegeName("정책법학대학");
        //when
        departmentRepository.save(department1);
        departmentRepository.save(department2);
        //then
        assertEquals(2,departmentRepository.findAll().size());
    }

    @Test
    @Rollback(false)
    public void findByCollege() throws Exception{
        //given
        Department department1 = new Department();
        department1.setName("소프트웨어학부");
        department1.setCollegeName("소프트웨어융합대학");

        Department department2 = new Department();
        department2.setName("법학부");
        department2.setCollegeName("정책법학대학");

        Department department3 = new Department();
        department3.setName("정보융합학부");
        department3.setCollegeName("소프트웨어융합대학");
        //when
        departmentRepository.save(department1);
        departmentRepository.save(department2);
        departmentRepository.save(department3);
        //then
        assertEquals(2,departmentRepository.findByCollege("소프트웨어융합대학").size());
    }

}