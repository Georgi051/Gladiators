package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.entities.Logger;
import project.gladiators.model.entities.User;
import project.gladiators.model.enums.Action;
import project.gladiators.repository.LoggerRepository;
import project.gladiators.service.LoggerService;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.time.LocalDateTime;

@Service
public class LoggerServiceImpl implements LoggerService {
    private final LoggerRepository loggerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LoggerServiceImpl(LoggerRepository loggerRepository, ModelMapper modelMapper) {
        this.loggerRepository = loggerRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void log(Action action, String description, LocalDateTime madeOn, UserServiceModel user) {
        Logger logger = new Logger();
        logger.setAction(action);
        logger.setDescription(description);
        logger.setMadeOn(madeOn);
        User currentUser = this.modelMapper.map(user, User.class);
        logger.setUser(currentUser);
        loggerRepository.save(logger);
    }
}
