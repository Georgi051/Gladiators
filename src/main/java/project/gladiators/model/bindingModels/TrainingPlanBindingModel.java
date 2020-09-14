package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import project.gladiators.model.entities.Workout;
import project.gladiators.model.enums.TrainingPlanType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TrainingPlanBindingModel {

    private TrainingPlanType trainingPlanType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startedOn;

    private List<Workout> workouts;

}
