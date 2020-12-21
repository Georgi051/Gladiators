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

    private String name;
    private List<Integer> sets;
    private List<String> repeats;
    private List<Integer> restTime;
    private List<Exercise> exercises;
}
