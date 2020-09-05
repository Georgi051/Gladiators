package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.TrainingPlan;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class TrainerServiceModel extends BaseServiceModel {


    private int yearsOfExperience;


    private String description;


    private Set<CustomerServiceModel> customers;


    private List<TrainingPlan> trainingPlans;


    private UserServiceModel userServiceModel;
}
