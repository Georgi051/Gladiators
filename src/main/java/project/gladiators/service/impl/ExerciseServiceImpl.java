package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.entities.Exercise;
import project.gladiators.repository.ExerciseRepository;
import project.gladiators.service.ExerciseService;
import project.gladiators.service.MuscleService;
import project.gladiators.service.serviceModels.ExerciseServiceModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final MuscleService muscleService;
    private final ModelMapper modelMapper;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, MuscleService muscleService, ModelMapper modelMapper) {
        this.exerciseRepository = exerciseRepository;
        this.muscleService = muscleService;
        this.modelMapper = modelMapper;
    }



    @Override
    public List<ExerciseServiceModel> findAll() {
        return this.exerciseRepository.findAll()
                .stream().map(exercise -> this.modelMapper.map(exercise, ExerciseServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void addExercise(ExerciseServiceModel exerciseServiceModel) {
        if (this.exerciseRepository.findByName(exerciseServiceModel.getName()).isPresent()) {
            return;
        }
        Exercise exercise = this.modelMapper.map(exerciseServiceModel, Exercise.class);
        this.exerciseRepository.saveAndFlush(exercise);
    }
}
