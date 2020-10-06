package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WorkoutExerciseInfoServiceModel extends BaseServiceModel{

    private ExerciseServiceModel exercise;
    private int sets;
    private String repeats;
    private int restTime;
    private WorkoutServiceModel workout;
}
