/*
package project.gladiators.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class BodyGoal  extends BaseEntity{
    private double desiredWeight;

    private double currentWeight;

    private double desiredHipSize;

    private double desiredWaistSize;

    private double actualHipSize;

    private double actualWaistSize;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private Customer customer;
}
*/
