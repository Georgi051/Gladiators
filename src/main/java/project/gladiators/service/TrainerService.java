package project.gladiators.service;

import project.gladiators.model.enums.Action;

public interface TrainerService {

    void changeTrainerStatus(String username, Action action);
}
