package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.dtos.MuscleDto;
import project.gladiators.model.entities.Muscle;
import project.gladiators.repository.MuscleRepository;
import project.gladiators.service.MuscleService;
import project.gladiators.service.serviceModels.ExerciseServiceModel;
import project.gladiators.service.serviceModels.MuscleServiceModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MuscleServiceImpl implements MuscleService {
    private final MuscleRepository muscleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MuscleServiceImpl(MuscleRepository muscleRepository, ModelMapper modelMapper) {
        this.muscleRepository = muscleRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void seedMuscles(MuscleDto[] muscles) {
        if (this.muscleRepository.count() != 0){
            return;
        }

        Arrays.stream(muscles).forEach(muscle -> {
            Muscle muscleToSave = new Muscle();
            muscleToSave.setName(muscle.getName());
            this.muscleRepository.saveAndFlush(muscleToSave);
        });
    }

    @Override
    public List<MuscleServiceModel> findAll() {
        return this.muscleRepository.findAll().stream()
                .map(muscle -> this.modelMapper.map(muscle,MuscleServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public MuscleServiceModel findByName(String name) {
        return this.modelMapper.map(this.muscleRepository.findByName(name),MuscleServiceModel.class);
    }
}
