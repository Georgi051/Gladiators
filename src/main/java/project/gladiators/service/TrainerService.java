package project.gladiators.service;

import org.springframework.web.multipart.MultipartFile;
import project.gladiators.model.enums.Action;
import project.gladiators.service.serviceModels.TrainerServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.io.IOException;
import java.util.List;

public interface TrainerService {

    void changeTrainerStatus(String username, Action action);

    void confirmTrainer(TrainerServiceModel trainerServiceModel, UserServiceModel userServiceModel, String username, MultipartFile profilePicture) throws IOException;

    List<TrainerServiceModel> findAll();

    TrainerServiceModel findById(String id);

    TrainerServiceModel findByUserName(String name);
}
