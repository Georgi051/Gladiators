package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.bindingModels.WorkoutAddBindingModel;
import project.gladiators.model.entities.WorkoutExerciseInfo;
import project.gladiators.repository.WorkoutExerciseInfoRepository;
import project.gladiators.service.WorkoutExerciseInfoService;
import project.gladiators.service.serviceModels.ExerciseServiceModel;
import project.gladiators.service.serviceModels.WorkoutExerciseInfoServiceModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class WorkoutExerciseInfoServiceImpl implements WorkoutExerciseInfoService {
    private final WorkoutExerciseInfoRepository workoutExerciseInfoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public WorkoutExerciseInfoServiceImpl(WorkoutExerciseInfoRepository workoutExerciseInfoRepository, ModelMapper modelMapper) {
        this.workoutExerciseInfoRepository = workoutExerciseInfoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedData(Set<WorkoutExerciseInfo> workoutExerciseInfos) {
        this.workoutExerciseInfoRepository.saveAll(workoutExerciseInfos);
    }

    @Override
    public List<WorkoutExerciseInfoServiceModel> addWorkoutExerciseInfoParams(WorkoutAddBindingModel workoutAddBindingModel) {
        List<WorkoutExerciseInfoServiceModel> workoutExerciseModels = new ArrayList<>(workoutAddBindingModel.getExercises().size());
        for (int i = 0; i < workoutAddBindingModel.getExercises().size(); i++) {
            WorkoutExerciseInfoServiceModel workoutExerciseInfoServiceModel = new WorkoutExerciseInfoServiceModel();
            workoutExerciseInfoServiceModel.setExercise(this.modelMapper
                    .map(workoutAddBindingModel.getExercises().get(i), ExerciseServiceModel.class));
            workoutExerciseInfoServiceModel.setRestTime(workoutAddBindingModel.getRestTime().get(i));
            workoutExerciseInfoServiceModel.setSets(workoutAddBindingModel.getSets().get(i));
            workoutExerciseInfoServiceModel.setRepeats(workoutAddBindingModel.getRepeats().get(i));
            workoutExerciseModels.add(workoutExerciseInfoServiceModel);
        }
        return workoutExerciseModels;
    }

}
