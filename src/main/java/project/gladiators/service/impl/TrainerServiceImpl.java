package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.constants.ExceptionMessages;
import project.gladiators.exceptions.InvalidChangeTrainerStatusException;
import project.gladiators.exceptions.TrainerNotFoundException;
import project.gladiators.model.entities.Trainer;
import project.gladiators.model.enums.Action;
import project.gladiators.repository.TrainerRepository;
import project.gladiators.service.RoleService;
import project.gladiators.service.TrainerService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.TrainerServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.io.IOException;

@Service
public class TrainerServiceImpl implements TrainerService {
    private TrainerRepository trainerRepository;
    private UserService userService;
    private RoleService roleService;
    private ModelMapper modelMapper;

    @Autowired
    public TrainerServiceImpl(TrainerRepository trainerRepository, UserService userService, RoleService roleService, ModelMapper modelMapper) {
        this.trainerRepository = trainerRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void changeTrainerStatus(String username, Action action) {
        UserServiceModel userServiceModel = userService.findUserByUsername(username);
        RoleServiceModel trainerConfirmed = this.roleService.findByAuthority("ROLE_TRAINER_CONFIRMED");
        RoleServiceModel trainerUnconfirmed = this.roleService.findByAuthority("ROLE_TRAINER_UNCONFIRMED");

        if (Action.valueOf("CREATE").equals(action)) {
            if (userServiceModel.getAuthorities().contains(trainerConfirmed)
                    || userServiceModel.getAuthorities().contains(trainerUnconfirmed)) {
                throw new InvalidChangeTrainerStatusException(ExceptionMessages.USER_ALREADY_TRAINER);
            }
            userServiceModel.getAuthorities().add(trainerUnconfirmed);

            TrainerServiceModel trainerServiceModel = new TrainerServiceModel();
            trainerServiceModel.setUserServiceModel(userServiceModel);

            Trainer trainer = this.modelMapper.map(trainerServiceModel, Trainer.class);
            trainerRepository.save(trainer);

        } else {
            if (!(userServiceModel.getAuthorities().contains(trainerConfirmed)
                    || userServiceModel.getAuthorities().contains(trainerUnconfirmed))) {
                throw new InvalidChangeTrainerStatusException(ExceptionMessages.USER_NOT_TRAINER);
            }
            userServiceModel.getAuthorities().remove(trainerConfirmed);
            userServiceModel.getAuthorities().remove(trainerUnconfirmed);
            trainerRepository.deleteTrainerByUser_Id(userServiceModel.getId());
        }

        userService.updateUser(userServiceModel);

    }

    @Override
    public void confirmTrainer(TrainerServiceModel trainerServiceModel, UserServiceModel userServiceModel, String username, MultipartFile profilePicture) throws IOException {

        Trainer trainer = this.trainerRepository.findTrainerByUser_Username(username).
                orElseThrow(() -> new TrainerNotFoundException(ExceptionMessages.TRAINER_NOT_FOUND));

        trainer.setDescription(trainerServiceModel.getDescription());
        trainer.setYearsOfExperience(trainerServiceModel.getYearsOfExperience());

        trainerRepository.save(trainer);


        userService.confirmTrainer(username, userServiceModel, profilePicture);


    }
}
