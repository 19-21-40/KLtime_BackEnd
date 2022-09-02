package domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class GradCondition {

    @Id
    @GeneratedValue
    @Column(name = "gradcondition_id")
    private Long id;
    private int addmissionYear;
    private int gradCredit;
    private int essBalCredit;
    private int basicCredit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;
    private int mainCredit;
    private boolean isMultiDept;
    private int multiCredit;
}
