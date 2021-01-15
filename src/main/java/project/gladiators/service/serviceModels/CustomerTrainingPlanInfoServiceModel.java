package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class CustomerTrainingPlanInfoServiceModel extends BaseServiceModel{

    private CustomerServiceModel customerServiceModel;
    private TrainingPlanServiceModel trainingPlanServiceModel;
    private LocalDate startedOn;
    private boolean isPaid;

}
