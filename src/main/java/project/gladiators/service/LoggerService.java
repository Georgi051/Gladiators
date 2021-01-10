package project.gladiators.service;

import project.gladiators.model.enums.Action;
import project.gladiators.service.serviceModels.AdminLogServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.time.LocalDateTime;
import java.util.List;

public interface LoggerService {

    void log(Action action, String description, LocalDateTime madeOn, UserServiceModel admin, UserServiceModel user);

    List<AdminLogServiceModel> findAll();
}
