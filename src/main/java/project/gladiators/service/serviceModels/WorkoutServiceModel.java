package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.enums.DayOfWeek;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class WorkoutServiceModel extends BaseServiceModel{

    private String name;

    private int duration;

    private DayOfWeek dayOfWeek;

    private Set<WorkoutExerciseInfoServiceModel> workoutExerciseInfo;

}
