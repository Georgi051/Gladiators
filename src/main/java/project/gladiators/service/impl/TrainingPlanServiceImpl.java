package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.bindingModels.TrainingPlanBindingModel;
import project.gladiators.model.entities.TrainingPlan;
import project.gladiators.model.entities.TrainingPlanWorkoutInfo;
import project.gladiators.model.entities.Workout;
import project.gladiators.repository.TrainingPlanRepository;
import project.gladiators.repository.TrainingPlanWorkoutInfoRepository;
import project.gladiators.service.TrainingPlanService;
import project.gladiators.service.WorkoutService;
import project.gladiators.service.serviceModels.TrainingPlanServiceModel;

import java.time.DayOfWeek;

@Service
public class TrainingPlanServiceImpl implements TrainingPlanService {
    private final TrainingPlanRepository trainingPlanRepository;
    private final WorkoutService workoutService;
    private final TrainingPlanWorkoutInfoRepository trainingPlanWorkoutInfoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TrainingPlanServiceImpl(TrainingPlanRepository trainingPlanRepository, WorkoutService workoutService, TrainingPlanWorkoutInfoRepository trainingPlanWorkoutInfoRepository, ModelMapper modelMapper) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.workoutService = workoutService;
        this.trainingPlanWorkoutInfoRepository = trainingPlanWorkoutInfoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addTrainingPlan(TrainingPlanBindingModel trainingInfoModel
            , TrainingPlanBindingModel trainingPlanWorkoutsModel) {

        TrainingPlanServiceModel trainingPlanServiceModel = this.modelMapper
                .map(trainingInfoModel, TrainingPlanServiceModel.class);
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        for (int i = 0; i < trainingPlanWorkoutsModel.getWorkout().size(); i++) {
            trainingPlanServiceModel.getWorkouts().add(new TrainingPlanWorkoutInfo());

            trainingPlanServiceModel.getWorkouts().get(i).setDayOfWeek(dayOfWeek);
            dayOfWeek = dayOfWeek.plus(1);
            Workout workout = this.modelMapper.map(this.workoutService
                    .findById(trainingPlanWorkoutsModel.getWorkout().get(i)), Workout.class);
            trainingPlanServiceModel.getWorkouts().get(i).setWorkout(workout);
        }
        trainingPlanServiceModel.getWorkouts()
                .forEach(this.trainingPlanWorkoutInfoRepository::saveAndFlush);
        this.trainingPlanRepository.saveAndFlush(this.modelMapper
        .map(trainingPlanServiceModel, TrainingPlan.class));

    }

    @Override
    public TrainingPlanServiceModel findById(String id) {

        return this.modelMapper
                .map(this.trainingPlanRepository
                .findById(id), TrainingPlanServiceModel.class);
    }
}
