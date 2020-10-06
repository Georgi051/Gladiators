package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class WorkoutServiceModel extends BaseServiceModel{

    private String name;

    private int duration;

    private DayOfWeek dayOfWeek;

    private List<WorkoutExerciseInfoServiceModel> workoutExerciseInfo;

}
