package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product extends BaseEntity {

    @Column(name = "product_name",nullable = false)
    private String name;

    @Column(name = "manufacturer_name",nullable = false)
    private String manufacturerName;

    @Column(nullable = false)
    private BigDecimal price;

    @Column()
    private Integer quantity;

    @Column(name = "buying_counter",nullable = false)
    private Integer buyingCounter;

    @Column(nullable = false)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany
    private List<Review> reviews;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Category> categories;

}
