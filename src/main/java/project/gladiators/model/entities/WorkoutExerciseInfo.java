package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class WorkoutExerciseInfo extends BaseEntity {


    @ManyToOne
    private Exercise exercise;

    @Column
    private int sets;

    @Column
    private int repeats;

    @Column
    private int restTime;


    @ManyToOne
    @JoinColumn(name = "workout_id",referencedColumnName = "id")
    private Workout workout;

}
