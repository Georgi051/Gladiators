package project.gladiators.model.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sub_categories")
@Getter
@Setter
@NoArgsConstructor
public class SubCategory extends BaseEntity{
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean isEmpty;

    @OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    private Set<Product> products = new HashSet<>();


}
