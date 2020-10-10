package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product extends BaseEntity {

    @Column
    private String name;

    @Column(name = "manufacturer_name")
    private String manufacturerName;

    @Column
    private BigDecimal price;

    @OneToMany
    List<Review> reviews;

    @Column
    String description;

    @Column
    private String imageUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Category> categories;

}
