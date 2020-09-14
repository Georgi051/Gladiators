package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.repository.TrainingPlanRepository;
import project.gladiators.service.TrainingPlanService;
import project.gladiators.service.serviceModels.TrainingPlanServiceModel;

@Service
public class TrainingPlanServiceImpl implements TrainingPlanService {
    private final TrainingPlanRepository trainingPlanRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TrainingPlanServiceImpl(TrainingPlanRepository trainingPlanRepository, ModelMapper modelMapper) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addTrainingPlan(TrainingPlanServiceModel trainingPlanServiceModel) {

    }
}
