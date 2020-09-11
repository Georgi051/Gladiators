package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.entities.Exercise;
import project.gladiators.model.entities.Workout;
import project.gladiators.repository.WorkoutRepository;
import project.gladiators.service.WorkoutService;
import project.gladiators.service.serviceModels.WorkoutServiceModel;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository, ModelMapper modelMapper) {
        this.workoutRepository = workoutRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addWorkout(WorkoutServiceModel workout) {
        Workout workoutToSave = this.modelMapper
                .map(workout, Workout.class);

        Set<Exercise> exercises = workout.getExercises()
                .stream()
                .map(exerciseServiceModel -> {
                    Exercise exercise = this.modelMapper
                            .map(exerciseServiceModel, Exercise.class);
                    return exercise;
                }).collect(Collectors.toSet());
        workoutToSave.setExercises(exercises);

        this.workoutRepository.saveAndFlush(workoutToSave);
    }
}
