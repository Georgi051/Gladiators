package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "offers")
@Getter
@Setter
@NoArgsConstructor
public class Offer extends BaseEntity {

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id"
    )
    private Product product;

    private BigDecimal price;

}
