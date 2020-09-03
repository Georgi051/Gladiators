package project.gladiators.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.model.dtos.MuscleDto;
import project.gladiators.model.bindingModels.UserRegisterBindingModel;
import project.gladiators.model.entities.User;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.io.IOException;
import java.util.List;

public interface UserService  extends UserDetailsService {
    UserServiceModel registerUser(UserServiceModel userServiceModel, UserRegisterBindingModel model, MuscleDto[] muscles);

    List<UserServiceModel> getAllUsers();

    UserServiceModel findById(String id);

    void addRoleToUser(UserServiceModel userServiceModel, RoleServiceModel roleServiceModel);

    void banUser(String id);

    UserServiceModel findUserByUsername(String username);

    void addUserAnotherData(User user, String firstName, String lastName, int age, String gender , MultipartFile image) throws IOException;

    void editUserProfile(UserServiceModel userServiceModel);

    void changeUserPassword(UserServiceModel userServiceModel, String oldPassword);
}
