package project.gladiators.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.enums.Action;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_log")
@Getter
@Setter
@NoArgsConstructor
public class Logger extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    private Action action;

    @Column
    private String description;

    @Column(name = "made_on")
    private LocalDateTime madeOn;

    @ManyToOne
    private User user;
}
