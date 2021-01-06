package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.constants.ExceptionMessages;
import project.gladiators.constants.RoleConstants;
import project.gladiators.exceptions.InvalidChangeTrainerStatusException;
import project.gladiators.exceptions.TrainerNotFoundException;
import project.gladiators.model.entities.Role;
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
import java.util.List;
import java.util.stream.Collectors;

import static project.gladiators.constants.ExceptionMessages.TRAINER_NOT_FOUND;
import static project.gladiators.constants.RoleConstants.TRAINER_CONFIRMED;
import static project.gladiators.constants.RoleConstants.TRAINER_UNCONFIRMED;

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
        RoleServiceModel trainerConfirmed = this.roleService.findByAuthority(RoleConstants.TRAINER_CONFIRMED);
        RoleServiceModel trainerUnconfirmed = this.roleService.findByAuthority(RoleConstants.TRAINER_UNCONFIRMED);

        if (Action.valueOf("CREATE").equals(action)) {
            if (userServiceModel.getAuthorities().contains(trainerConfirmed)
                    || userServiceModel.getAuthorities().contains(trainerUnconfirmed)) {
                throw new InvalidChangeTrainerStatusException(ExceptionMessages.USER_ALREADY_TRAINER);
            }
            userServiceModel.getAuthorities().add(trainerUnconfirmed);

            TrainerServiceModel trainerServiceModel = new TrainerServiceModel();
            trainerServiceModel.setUser(userServiceModel);

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
                orElseThrow(() -> new TrainerNotFoundException(TRAINER_NOT_FOUND));

        trainer.setDescription(trainerServiceModel.getDescription());
        trainer.setYearsOfExperience(trainerServiceModel.getYearsOfExperience());

        trainerRepository.save(trainer);

        userService.confirmTrainer(username, userServiceModel, profilePicture);

    }

    @Override
    public List<TrainerServiceModel> findAll() {

        List<Trainer> trainers = trainerRepository.findAll()
                .stream().
                        filter(e -> !e.getUser().getAuthorities().contains(new Role(RoleConstants.TRAINER_UNCONFIRMED))).
                        collect(Collectors.toList());

        if (trainers.size() == 0) {
            throw new TrainerNotFoundException("There are not registered any trainers yet");
        }
        return List.of(this.modelMapper.map(trainers, TrainerServiceModel[].class));
    }

    @Override
    public TrainerServiceModel findById(String id) {

        Trainer trainer = this.trainerRepository.findById(id)
                .orElseThrow(()-> new TrainerNotFoundException(TRAINER_NOT_FOUND));
        TrainerServiceModel trainerServiceModel = this.modelMapper
                .map(trainer, TrainerServiceModel.class);
        return trainerServiceModel;
    }

}
