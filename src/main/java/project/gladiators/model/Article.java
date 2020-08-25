package project.gladiators.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Article extends BaseEntity {

    private String title;

    private String description;

    private LocalDateTime addedOn;

    @ManyToOne
    private Customer customer;
}
