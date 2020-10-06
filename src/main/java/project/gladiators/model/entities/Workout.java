package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.time.DayOfWeek;
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

    @ManyToOne
    private TrainingPlanWorkoutInfo trainingPlanWorkoutInfo;

    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    private Set<WorkoutExerciseInfo> workoutExerciseInfos;

}
