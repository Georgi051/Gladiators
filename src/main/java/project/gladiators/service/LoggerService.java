package project.gladiators.service;

import project.gladiators.model.enums.Action;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.time.LocalDateTime;

public interface LoggerService {

    void log(Action action, String description, LocalDateTime madeOn, UserServiceModel user);
}
