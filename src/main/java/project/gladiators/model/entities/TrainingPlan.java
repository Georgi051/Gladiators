package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.enums.TrainingPlanType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "training_plans")
@Getter
@Setter
@NoArgsConstructor
public class TrainingPlan extends BaseEntity{

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "training_plan_type")
    private TrainingPlanType trainingPlanType;

    @Column(name = "started_on")
    private LocalDate startedOn;

    @ManyToMany(cascade = CascadeType.ALL,
    fetch = FetchType.EAGER)
    private List<TrainingPlanWorkoutInfo> workouts;

    @OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    private Set<Customer> customers = new HashSet<>();

}
