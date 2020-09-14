package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.entities.Exercise;
import project.gladiators.model.entities.Workout;
import project.gladiators.model.entities.WorkoutExerciseInfo;
import project.gladiators.repository.WorkoutExerciseInfoRepository;
import project.gladiators.repository.WorkoutRepository;
import project.gladiators.service.WorkoutExerciseInfoService;
import project.gladiators.service.WorkoutService;
import project.gladiators.service.serviceModels.WorkoutServiceModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseInfoService workoutExerciseInfoService;
    private final ModelMapper modelMapper;

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository, WorkoutExerciseInfoService workoutExerciseInfoService, ModelMapper modelMapper) {
        this.workoutRepository = workoutRepository;
        this.workoutExerciseInfoService = workoutExerciseInfoService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addWorkout(WorkoutServiceModel workout, Set<Exercise> exercises) {
        Workout workoutToSave = this.modelMapper
                .map(workout, Workout.class);

      Set<WorkoutExerciseInfo> workoutExerciseInfos = new HashSet<>();

      exercises.forEach(exercise -> {
          WorkoutExerciseInfo workoutExerciseInfo =
                  new WorkoutExerciseInfo();
          workoutExerciseInfo.setExercise(exercise);
          workoutExerciseInfos.add(workoutExerciseInfo);
      });

        workoutToSave.setWorkoutExerciseInfos(workoutExerciseInfos);
        this.workoutExerciseInfoService.seedData(workoutExerciseInfos);
        this.workoutRepository.saveAndFlush(workoutToSave);
    }

    @Override
    public List<WorkoutServiceModel> findAll() {
        return this.workoutRepository.findAll().stream()
                .map(workout -> this.modelMapper.map(workout,WorkoutServiceModel.class))
                .collect(Collectors.toList());
    }
}
