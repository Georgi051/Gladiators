package project.gladiators.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Exercise extends BaseEntity {

    private String name;
    private String description;

    @ManyToMany
    private List<Muscle> muscles;
    private String url;

}
