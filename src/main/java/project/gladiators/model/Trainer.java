package project.gladiators.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "trainers")
@Getter
@Setter
@NoArgsConstructor
public class Trainer extends BaseEntity{

    @Column(name = "years_of_experience", nullable = false)
    private int yearsOfExperience;

    @Column
    private String description;

    @OneToMany
    private Set<Customer> customers;

    @OneToMany
    private List<TrainingPlan> trainingPlans;


    @OneToOne
    private User user;

}
