package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.dtos.ExerciseDto;
import project.gladiators.model.entities.Exercise;
import project.gladiators.repository.ExerciseRepository;
import project.gladiators.service.ExerciseService;
import project.gladiators.service.serviceModels.ExerciseServiceModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ModelMapper modelMapper) {
        this.exerciseRepository = exerciseRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedExercise(ExerciseDto[] exercises) {
        if (this.exerciseRepository.count() != 0){
            return;
        }

        Arrays.stream(exercises).forEach(exerciseDto -> {
            Exercise exercise = new Exercise();
            exercise.setName(exerciseDto.getName());
            exercise.setDescription(exerciseDto.getDescription());
            exercise.setImageUrl(exerciseDto.getImageUrl());
            exercise.setMuscles(null);
            this.exerciseRepository.saveAndFlush(exercise);
        });
    }

    @Override
    public List<ExerciseServiceModel> findAll() {
        return this.exerciseRepository.findAll()
                .stream().map(exercise -> this.modelMapper.map(exercise,ExerciseServiceModel.class))
                .collect(Collectors.toList());
    }
}
