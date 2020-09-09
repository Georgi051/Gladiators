package project.gladiators.service;

import project.gladiators.model.dtos.MuscleDto;
import project.gladiators.service.serviceModels.MuscleServiceModel;

import java.util.List;

public interface MuscleService {
    void seedMuscles(MuscleDto[] muscles);

    List<MuscleServiceModel> findAll();

    MuscleServiceModel findByName(String name);
}
