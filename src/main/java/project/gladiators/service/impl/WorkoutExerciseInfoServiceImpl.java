package project.gladiators.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.entities.WorkoutExerciseInfo;
import project.gladiators.repository.WorkoutExerciseInfoRepository;
import project.gladiators.service.WorkoutExerciseInfoService;

import java.util.Set;

@Service
public class WorkoutExerciseInfoServiceImpl implements WorkoutExerciseInfoService {
    private final WorkoutExerciseInfoRepository workoutExerciseInfoRepository;

    @Autowired
    public WorkoutExerciseInfoServiceImpl(WorkoutExerciseInfoRepository workoutExerciseInfoRepository) {
        this.workoutExerciseInfoRepository = workoutExerciseInfoRepository;
    }

    @Override
    public void seedData(Set<WorkoutExerciseInfo> workoutExerciseInfos) {
        this.workoutExerciseInfoRepository.saveAll(workoutExerciseInfos);
    }
}
