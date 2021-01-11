package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class CustomerTrainingPlanInfoServiceModel extends BaseServiceModel{

    private CustomerServiceModel customerServiceModel;
    private TrainingPlanServiceModel trainingPlanServiceModel;
    private LocalDate startedOn;
}
