package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "workouts")
@Getter
@Setter
@NoArgsConstructor
public class Workout extends BaseEntity {

    @Column
    private String name;

    @Column
    private int duration;

    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    private Set<WorkoutExerciseInfo> workoutExerciseInfos;

}
