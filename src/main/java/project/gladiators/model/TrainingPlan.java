package project.gladiators.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.enums.Difficulty;
import project.gladiators.model.enums.TrainingPlanType;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "training_plans")
@Getter
@Setter
@NoArgsConstructor
public class TrainingPlan extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "training_plan_type")
    private TrainingPlanType trainingPlanType;

    @Enumerated(EnumType.STRING)
    @Column
    private Difficulty difficulty;

    @ManyToMany(cascade = CascadeType.MERGE,
    fetch = FetchType.EAGER)
    private List<Exercise> exercises;

}
