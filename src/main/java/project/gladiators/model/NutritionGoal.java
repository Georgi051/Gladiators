/*
package project.gladiators.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class NutritionGoal  extends BaseEntity{

    private int desiredCalorieIntake;
    private int desiredFatIntake;
    private int desiredCarbsIntake;
    private int desiredProteinIntake;
    private int currentCalorieIntake;
    private int currentFatIntake;
    private int currentCarbsIntake;
    private int currentProteinIntake;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private Customer customer;


}
*/
