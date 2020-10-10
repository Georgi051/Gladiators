package project.gladiators.model.entities;

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
    private User user;

    @Column
    private LocalDateTime madeOn;

}
