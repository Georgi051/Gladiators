package project.gladiators.service;

import project.gladiators.model.enums.Action;
import project.gladiators.service.serviceModels.TrainerServiceModel;

public interface TrainerService {

    void changeTrainerStatus(String username, Action action);

    void confirmTrainer(TrainerServiceModel trainerServiceModel,String username );

}
