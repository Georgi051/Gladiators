package project.gladiators.service;

import project.gladiators.model.bindingModels.TrainingPlanBindingModel;
import project.gladiators.model.entities.Exercise;
import project.gladiators.model.entities.Workout;
import project.gladiators.service.serviceModels.WorkoutExerciseInfoServiceModel;
import project.gladiators.service.serviceModels.WorkoutServiceModel;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface WorkoutService {

    void addWorkout(WorkoutServiceModel workout, List<Exercise> exercises);

    void addWorkoutToTrainingPlan(WorkoutServiceModel workout, List<Exercise> exercises, TrainingPlanBindingModel trainingPlan);


    List<WorkoutServiceModel> findAll();

    WorkoutServiceModel findById(String id);
}
