package com.example.demo.Repository;

import com.example.demo.domain.Department;
import com.example.demo.domain.Lecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DepartmentRepository {

//    @PersistenceContext
    private final EntityManager em;

    /**
     * 학과(부) 저장
     */
    public Long save(Department department) {
//        validateDuplicateDepartment(department); //나중에 서비스 레포지토리에 옮기기
        em.persist(department);
        return department.getId();
    }

    /**
     * 나중에 서비스레포지토리에 옮기기
     */
//    private void validateDuplicateDepartment(Department department) {
//        List<Department> findDepartments = findByName(department.getName());
//        if (!findDepartments.isEmpty()) {
//            throw new IllegalStateException("이미 존재하는 학과(부)입니다.");
//        }
//    }

    /**
     * 이름으로 학과(부) 조회 (중복 검증때문에 추가함)
     */
    public List<Department> findByName(String name){
        return em.createQuery("select d from Department d where d.name = :name", Department.class)
                .setParameter("name", name)
                .getResultList();
    }


    /**
     * 학과(부) 조회
     */
    public Department findById(Long id) {
        return em.find(Department.class, id);
    }

    /**
     * 학과(부) 전체 조회
     */
    public List<Department> findAll(){
        return em.createQuery("select d from Department d",Department.class)
                .getResultList();
    }

    /**
     * 단과대 소속의 학과(부)들 조회
     */
    public List<Department> findByCollege(String collegeName){
        return em.createQuery("select d from Department d where d.collegeName = :collegeName", Department.class)
                .setParameter("collegeName", collegeName)
                .getResultList();
    }
}
