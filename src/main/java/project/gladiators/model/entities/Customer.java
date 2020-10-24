package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer extends BaseEntity{

    @Column
    private double weight;

    @Column
    private double height;

    @Column
    private double BMI;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private ProgressChart progressChart;

    @OneToOne
    private User user;


}
