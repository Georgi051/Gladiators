package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.Exercise;

import javax.validation.constraints.*;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class WorkoutAddBindingModel {

    @Size(min = 3, message = "Exercise name must be at least 3 characters")
    private String name;

/*    @Min(value = 1 ,message = "Sets must be at least one")*/
    private List<Integer> sets;

    private List<String> repeats;

    private List<Integer> restTime;
/*

    @NotNull(message = "Please choose day of week")
    private DayOfWeek dayOfWeek;
*/

    @NotEmpty(message = "You must select exercise one or more.")
    private List<Exercise> exercises;
}
