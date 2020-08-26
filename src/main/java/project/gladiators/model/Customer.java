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
public class Customer extends BaseEntity{

    @OneToMany(mappedBy = "customer"
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

    @OneToOne
    private User user;


}
