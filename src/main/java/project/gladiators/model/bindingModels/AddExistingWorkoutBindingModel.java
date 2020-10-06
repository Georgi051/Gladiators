package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.Workout;


@NoArgsConstructor
@Getter
@Setter
public class AddExistingWorkoutBindingModel {

    private Workout workout;
}
