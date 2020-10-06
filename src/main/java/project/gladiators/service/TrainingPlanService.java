package project.gladiators.service;

import project.gladiators.model.bindingModels.TrainingPlanBindingModel;
import project.gladiators.model.entities.TrainingPlan;
import project.gladiators.service.serviceModels.TrainingPlanServiceModel;

public interface TrainingPlanService {

    void addTrainingPlan(TrainingPlanBindingModel trainingInfoModel, TrainingPlanBindingModel trainingPlanWorkoutsModel);

    TrainingPlanServiceModel findById(String id);

}
