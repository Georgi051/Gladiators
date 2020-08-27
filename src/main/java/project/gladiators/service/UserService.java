package project.gladiators.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import project.gladiators.model.bindingModels.UserRegisterBindingModel;
import project.gladiators.service.serviceModels.UserServiceModel;

public interface UserService  extends UserDetailsService {
    UserServiceModel registerUser(UserServiceModel userServiceModel, UserRegisterBindingModel model);
}
