package project.gladiators.service;

import org.springframework.web.multipart.MultipartFile;
import project.gladiators.service.serviceModels.ExerciseServiceModel;

import java.io.IOException;
import java.util.List;

public interface ExerciseService {


    List<ExerciseServiceModel> findAll();

    void addExercise(ExerciseServiceModel exerciseServiceModel);
}
