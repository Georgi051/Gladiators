package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<SubCategory> subCategories;
}
