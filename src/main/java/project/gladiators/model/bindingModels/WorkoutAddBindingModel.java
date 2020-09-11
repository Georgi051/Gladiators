package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.Exercise;
import project.gladiators.service.serviceModels.ExerciseServiceModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class WorkoutAddBindingModel {

    @Size(min = 3, message = "Exercise name must be at least 3 characters")
    private String name;

    @Positive(message = "Duration must be positive number")
    private int duration;

    @NotEmpty(message = "You must select exercise one or more.")
    private Set<Exercise> exercises;
}