package project.gladiators.web.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.TrainingPlanWorkoutInfo;
import project.gladiators.model.enums.TrainingPlanType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TrainingPlanViewModel extends BaseViewModel{
    private String name;

    private TrainingPlanType trainingPlanType;

    private LocalDate startedOn;

    private List<TrainingPlanWorkoutInfo> workouts = new ArrayList<>();
}
