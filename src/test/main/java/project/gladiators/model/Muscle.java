package project.gladiators.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "muscles")
@Getter
@Setter
@NoArgsConstructor
public class Muscle extends BaseEntity {

    @Column
    private String name;

}
