package project.gladiators.service;

import project.gladiators.service.serviceModels.ExerciseServiceModel;

import java.util.List;

public interface ExerciseService {


    List<ExerciseServiceModel> findAll();

    void addExercise(ExerciseServiceModel exerciseServiceModel);
}
