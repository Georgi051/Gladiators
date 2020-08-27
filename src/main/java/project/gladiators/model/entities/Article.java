package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
public class Article extends BaseEntity {

    @Column
    private String title;

    @Column
    private String description;

    @Column(name = "added_on")
    private LocalDateTime addedOn;

    @ManyToOne
    private User user;
}
