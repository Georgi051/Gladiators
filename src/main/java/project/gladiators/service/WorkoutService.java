package project.gladiators.service;

import project.gladiators.model.dtos.MuscleDto;
import project.gladiators.model.dtos.WorkoutDto;

public interface WorkoutService {

    void seedWorkouts(WorkoutDto[] workouts);
}
