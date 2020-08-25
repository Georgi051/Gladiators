package project.gladiators.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer extends User{


  /*  @Column(name = "body_goal")
    @OneToOne
    private BodyGoal bodyGoal;

    @Column(name = "nutrition_goal")
    @OneToOne
    private NutritionGoal nutritionGoal;
*/
    @OneToMany(mappedBy = "author"
            ,cascade = CascadeType.ALL
            ,orphanRemoval = true
            ,fetch = FetchType.EAGER)
    private List<Order> orders;

    @Column
    private double weight;

    @Column
    private double height;

    @Column
    private double BMI;

    @OneToOne
    private ProgressChart progressChart;
    @Override
    public Set<Role> getAuthorities() {
        return super.getAuthorities();
    }
}
