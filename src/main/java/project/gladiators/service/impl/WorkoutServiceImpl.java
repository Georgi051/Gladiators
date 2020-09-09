package project.gladiators.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.dtos.MuscleDto;
import project.gladiators.model.dtos.WorkoutDto;
import project.gladiators.model.entities.Muscle;
import project.gladiators.model.entities.Workout;
import project.gladiators.repository.MuscleRepository;
import project.gladiators.repository.WorkoutRepository;
import project.gladiators.service.WorkoutService;

import java.util.Arrays;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }


    @Override
    public void seedWorkouts(WorkoutDto[] workouts) {
        if (this.workoutRepository.count() != 0){
            return;
        }

        Arrays.stream(workouts).forEach(workout -> {
            Workout disease = new Workout();
            disease.setName(workout.getName());
            disease.setDuration(workout.getDuration());
            this.workoutRepository.saveAndFlush(disease);
        });
    }

}
