package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.bindingModels.TrainingPlanBindingModel;
import project.gladiators.model.entities.Exercise;
import project.gladiators.model.entities.Workout;
import project.gladiators.model.entities.WorkoutExerciseInfo;
import project.gladiators.repository.WorkoutRepository;
import project.gladiators.service.WorkoutExerciseInfoService;
import project.gladiators.service.WorkoutService;
import project.gladiators.service.serviceModels.ExerciseServiceModel;
import project.gladiators.service.serviceModels.WorkoutExerciseInfoServiceModel;
import project.gladiators.service.serviceModels.WorkoutServiceModel;

import java.time.DayOfWeek;
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
    public void addWorkout(WorkoutServiceModel workout, List<Exercise> exercises) {
        Workout workoutToSave = this.modelMapper
                .map(workout, Workout.class);

        Set<WorkoutExerciseInfo> exerciseInfos = new HashSet<>();
        for (int i = 0; i < exercises.size(); i++) {
            workout.getWorkoutExerciseInfo().get(i).setExercise(this.modelMapper
            .map(exercises.get(i), ExerciseServiceModel.class));
        }

        for (WorkoutExerciseInfoServiceModel workoutExerciseInfoServiceModel : workout.getWorkoutExerciseInfo()) {
            WorkoutExerciseInfo workoutExerciseInfo = this.modelMapper
                    .map(workoutExerciseInfoServiceModel, WorkoutExerciseInfo.class);
            exerciseInfos.add(workoutExerciseInfo);
        }

        int allSets = 0;
        int allRestTime = 0;
        for (WorkoutExerciseInfoServiceModel workoutExerciseInfo : workout.getWorkoutExerciseInfo()) {
            allSets += workoutExerciseInfo.getSets();
            allRestTime += workoutExerciseInfo.getRestTime();
        }
        int duration = (allSets * 2) + allRestTime;
        workoutToSave.setDuration(duration);
        workoutToSave.setWorkoutExerciseInfos(exerciseInfos);
        this.workoutExerciseInfoService.seedData(exerciseInfos);
        this.workoutRepository.saveAndFlush(workoutToSave);
    }

    @Override
    public void addWorkoutToTrainingPlan(WorkoutServiceModel workout, List<Exercise> exercises, TrainingPlanBindingModel trainingPlan) {

        Set<WorkoutExerciseInfo> workoutExerciseInfos = new HashSet<>();

        exercises.forEach(exercise -> {
            WorkoutExerciseInfo workoutExerciseInfo =
                    new WorkoutExerciseInfo();
            workoutExerciseInfo.setExercise(exercise);
            workoutExerciseInfos.add(workoutExerciseInfo);
        });

        Workout currentWorkout = this.modelMapper
                .map(workout, Workout.class);
        currentWorkout.setWorkoutExerciseInfos(workoutExerciseInfos);

        trainingPlan.getWorkout().add(currentWorkout.getName());


    }

    @Override
    public List<WorkoutServiceModel> findAll() {
        return this.workoutRepository.findAll().stream()
                .map(workout -> this.modelMapper.map(workout,WorkoutServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public WorkoutServiceModel findById(String id) {
       Workout workout = this.workoutRepository.findById(id).orElse(null);

       return this.modelMapper
               .map(workout, WorkoutServiceModel.class);
    }
}
