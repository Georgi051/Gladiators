package project.gladiators.service;

import project.gladiators.model.bindingModels.TrainingPlanBindingModel;
import project.gladiators.model.entities.TrainingPlan;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.TrainingPlanServiceModel;

import javax.servlet.http.HttpSession;
import java.security.Principal;

public interface TrainingPlanService {

    void addTrainingPlan(TrainingPlanServiceModel trainingPlanServiceModel, Principal principal);

    TrainingPlanServiceModel findById(String id);

    void addWorkoutsToTrainingPlanByDay(TrainingPlanBindingModel trainingPlanBindingModel,
                                        HttpSession httpSession, Principal principal);

    TrainingPlanServiceModel findByCustomer(CustomerServiceModel customerServiceModel);
}
