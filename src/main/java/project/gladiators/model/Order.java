package project.gladiators.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
public class Order extends BaseEntity {

    @ManyToMany(cascade = CascadeType.MERGE
            ,fetch = FetchType.EAGER)
    private List<Product> products;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Trainer trainer;

    @Column
    private LocalDateTime madeOn;

}
