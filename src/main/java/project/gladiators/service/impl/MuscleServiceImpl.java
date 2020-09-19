package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.repository.MuscleRepository;
import project.gladiators.service.MuscleService;
import project.gladiators.service.serviceModels.MuscleServiceModel;

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
