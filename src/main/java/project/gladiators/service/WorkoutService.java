package project.gladiators.service;

import project.gladiators.model.entities.Exercise;
import project.gladiators.service.serviceModels.WorkoutServiceModel;

import java.util.List;
import java.util.Set;

public interface WorkoutService {

    void addWorkout(WorkoutServiceModel workout, Set<Exercise> exercises);

    List<WorkoutServiceModel> findAll();
}
