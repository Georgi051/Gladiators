package project.gladiators.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.dtos.MuscleDto;
import project.gladiators.model.entities.Muscle;
import project.gladiators.repository.MuscleRepository;
import project.gladiators.service.MuscleService;

import java.util.Arrays;

@Service
public class MuscleServiceImpl implements MuscleService {
    private final MuscleRepository muscleRepository;

    @Autowired
    public MuscleServiceImpl(MuscleRepository muscleRepository) {
        this.muscleRepository = muscleRepository;
    }


    @Override
    public void seedMuscles(MuscleDto[] muscles) {
        if (this.muscleRepository.count() != 0){
            return;
        }

        Arrays.stream(muscles).forEach(muscle -> {
            Muscle disease = new Muscle();
            disease.setName(muscle.getName());
            this.muscleRepository.saveAndFlush(disease);
        });
    }
}
