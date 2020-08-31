package project.gladiators.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import project.gladiators.model.bindingModels.UserRegisterBindingModel;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.util.List;

public interface UserService  extends UserDetailsService {
    UserServiceModel registerUser(UserServiceModel userServiceModel, UserRegisterBindingModel model);

    List<UserServiceModel> getAllUsers();

    UserServiceModel findById(String id);

    void addRoleToUser(UserServiceModel userServiceModel, RoleServiceModel roleServiceModel);
}
