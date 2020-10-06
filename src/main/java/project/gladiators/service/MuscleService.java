package project.gladiators.service;

import project.gladiators.service.serviceModels.MuscleServiceModel;

import java.util.List;

public interface MuscleService {


    List<MuscleServiceModel> findAll();

    MuscleServiceModel findByName(String name);
}
