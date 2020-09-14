package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.Workout;
import project.gladiators.model.enums.TrainingPlanType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TrainingPlanServiceModel extends BaseServiceModel{
    private TrainingPlanType trainingPlanType;

    private LocalDate startedOn;

    private List<WorkoutServiceModel> workouts;
}
