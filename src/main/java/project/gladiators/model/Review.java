package project.gladiators.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Review extends BaseEntity {

    private String title;
    private String description;
    private int stars;

    @ManyToOne
    @JoinColumn(name = "author_id",referencedColumnName = "id")
    private Customer author;


}
