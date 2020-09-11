package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class WorkoutServiceModel extends BaseServiceModel{

    private String name;
    private int duration;
    private Set<ExerciseServiceModel> exercises;

}
