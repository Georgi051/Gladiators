package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class WorkoutExerciseInfo extends BaseEntity {


    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    private Exercise exercise;

    @Column
    private int sets;

    @Column
    private String repeats;

    @Column
    private int restTime;


}
