package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.entities.Logger;
import project.gladiators.model.entities.User;
import project.gladiators.model.enums.Action;
import project.gladiators.repository.LoggerRepository;
import project.gladiators.service.LoggerService;
import project.gladiators.service.serviceModels.AdminLogServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public void log(Action action, String description, LocalDateTime madeOn, UserServiceModel admin, UserServiceModel user) {
        Logger logger = new Logger();
        logger.setAction(action);
        logger.setDescription(description);
        logger.setMadeOn(madeOn);
        logger.setUser(this.modelMapper.map(user, User.class));
        logger.setAdmin(this.modelMapper.map(admin, User.class));
        loggerRepository.save(logger);
    }

    @Override
    public List<AdminLogServiceModel> findAll() {
        return this.loggerRepository.findAll().stream()
                .map(logger -> this.modelMapper.map(logger, AdminLogServiceModel.class))
                .collect(Collectors.toList());
    }
}
