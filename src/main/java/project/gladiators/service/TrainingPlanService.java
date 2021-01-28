package project.gladiators.service;

import project.gladiators.model.bindingModels.TrainingPlanBindingModel;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.TrainingPlanServiceModel;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface TrainingPlanService {

    void addTrainingPlan(TrainingPlanServiceModel trainingPlanServiceModel, String username);

    TrainingPlanServiceModel findById(String id);

    void addWorkoutsToTrainingPlanByDay(TrainingPlanBindingModel trainingPlanBindingModel,
                                        HttpSession httpSession, String username);

    List<TrainingPlanServiceModel> findAll();

    boolean addTrainingPlanToCustomer(String customerId, String trainingPlanName, String trainerName, LocalDate startedOn);

    TrainingPlanServiceModel findByCustomer(CustomerServiceModel customerServiceModel);
}
