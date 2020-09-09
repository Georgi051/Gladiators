package project.gladiators.service;

import project.gladiators.model.dtos.ExerciseDto;
import project.gladiators.service.serviceModels.ExerciseServiceModel;

import java.util.List;

public interface ExerciseService {
    void seedExercise(ExerciseDto[] muscles);
    List<ExerciseServiceModel> findAll();
}
