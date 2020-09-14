package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.entities.WorkoutExerciseInfo;
import project.gladiators.repository.WorkoutExerciseInfoRepository;
import project.gladiators.service.WorkoutExerciseInfoService;

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
}
