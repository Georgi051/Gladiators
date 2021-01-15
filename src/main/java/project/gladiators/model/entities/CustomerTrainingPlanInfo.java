package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customer_training_plan_info")
public class CustomerTrainingPlanInfo extends BaseEntity{

    @OneToOne
    private TrainingPlan trainingPlan;

    @OneToOne
    private Customer customer;

    @Column(name = "started_on")
    private LocalDate startedOn;

    @Column(name = "is_paid")
    private boolean isPaid;

}
