package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.gladiators.model.bindingModels.UserRegisterBindingModel;
import project.gladiators.model.entities.User;
import project.gladiators.repository.UserRepository;
import project.gladiators.service.RoleService;
import project.gladiators.service.serviceModels.UserServiceModel;
import project.gladiators.service.UserService;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel, UserRegisterBindingModel regUser) {
        if (!regUser.getPassword().equals(regUser.getConfirmPassword())) {
            return null;
        }

        if (this.userRepository.count() == 0) {
            this.roleService.seedRoleInDb();
            userServiceModel.setAuthorities(new HashSet<>());
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority(("ROLE_ROOT")));
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority(("ROLE_ADMIN")));
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority(("ROLE_MODERATOR")));
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority(("ROLE_USER")));
        } else {
            userServiceModel.setAuthorities(new HashSet<>());
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority(("ROLE_USER")));
        }

        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));


        return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findUserByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
