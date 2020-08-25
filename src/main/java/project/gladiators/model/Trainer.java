package project.gladiators.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "trainers")
@Getter
@Setter
@NoArgsConstructor
public class Trainer extends User{

    @Column(name = "years_of_experience", nullable = false)
    private int yearsOfExperience;

    private String description;

    @OneToMany
    private Set<Customer> customers;

    @OneToMany
    private List<TrainingPlan> trainingPlans;

}
