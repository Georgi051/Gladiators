package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
public class Order extends BaseEntity {

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.REMOVE}
            ,fetch = FetchType.EAGER)
    @JoinTable(name = "orders_products")
    private List<Product> products;

    @ManyToOne
    private User customer;

    @Column
    private LocalDateTime madeOn;

    @Column
    private BigDecimal totalPrice;

}
