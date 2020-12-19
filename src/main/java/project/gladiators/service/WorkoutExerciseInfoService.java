package project.gladiators.service;

import project.gladiators.model.bindingModels.WorkoutAddBindingModel;
import project.gladiators.model.entities.WorkoutExerciseInfo;
import project.gladiators.service.serviceModels.WorkoutExerciseInfoServiceModel;

import java.util.List;
import java.util.Set;

public interface WorkoutExerciseInfoService {

   void seedData(Set<WorkoutExerciseInfo> workoutExerciseInfos);

   List<WorkoutExerciseInfoServiceModel> addWorkoutExerciseInfoParams(WorkoutAddBindingModel workoutAddBindingModel);
}
