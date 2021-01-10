package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.Workout;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import java.time.DayOfWeek;

@NoArgsConstructor
@Getter
@Setter
public class TrainingPlanWorkoutInfoServiceModel extends BaseServiceModel{

    WorkoutServiceModel workout;

    DayOfWeek dayOfWeek;
}
