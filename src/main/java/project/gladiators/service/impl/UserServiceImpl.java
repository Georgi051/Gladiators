package project.gladiators.service.impl;

import com.cloudinary.Cloudinary;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.exceptions.UserNotFoundException;
import project.gladiators.model.dtos.MuscleDto;
import project.gladiators.model.bindingModels.UserRegisterBindingModel;
import project.gladiators.model.dtos.WorkoutDto;
import project.gladiators.model.entities.Role;
import project.gladiators.model.entities.User;
import project.gladiators.model.enums.Gender;
import project.gladiators.repository.UserRepository;
import project.gladiators.service.*;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final MuscleService muscleService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final WorkoutService workoutService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, MuscleService muscleService, CloudinaryService cloudinaryService, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder, WorkoutService workoutService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.muscleService = muscleService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.workoutService = workoutService;
    }


    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel, UserRegisterBindingModel regUser, MuscleDto[] muscles, WorkoutDto[] workouts) {
        if (!regUser.getPassword().equals(regUser.getConfirmPassword())) {
            return null;
        }

        if (this.userRepository.count() == 0) {
            this.roleService.seedRoleInDb();
            this.muscleService.seedMuscles(muscles);
            this.workoutService.seedWorkouts(workouts);
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
        user.setRegisteredOn(LocalDateTime.now());

        return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        List<UserServiceModel> users = this.userRepository
                .findAll()
                .stream()
                .map(user -> {
                    UserServiceModel userServiceModel =
                            this.modelMapper
                                    .map(user, UserServiceModel.class);
                    return userServiceModel;
                }).collect(Collectors.toList());

        return users;
    }

    @Override
    public UserServiceModel findById(String id) {

        User user = this.userRepository.findById(id).orElse(null);
        return this.modelMapper
                .map(user, UserServiceModel.class);
    }

    @Override
    public void addRoleToUser(UserServiceModel userServiceModel, RoleServiceModel roleServiceModel) {
        User user = this.userRepository
                .findUserByUsername(userServiceModel.getUsername()).orElse(null);
        RoleServiceModel role = this.roleService
                .findByAuthority(roleServiceModel.getAuthority());
        if (user != null) {
            boolean isCustomer = false;
            for (Role authority : user.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_CUSTOMER")) {
                    isCustomer = true;
                    break;
                }
            }
            user.getAuthorities().clear();
            if(isCustomer){
                switch (role.getAuthority()) {
                    case "ROLE_USER":
                        user.getAuthorities().add(this.modelMapper
                                .map(roleService.findByAuthority("ROLE_USER"), Role.class));
                        user.getAuthorities().add(this.modelMapper
                        .map(roleService.findByAuthority("ROLE_CUSTOMER"), Role.class));
                        break;
                    case "ROLE_MODERATOR":
                        user.getAuthorities().add(this.modelMapper
                                .map(roleService.findByAuthority("ROLE_USER"), Role.class));
                        user.getAuthorities().add(this.modelMapper
                                .map(roleService.findByAuthority("ROLE_MODERATOR"), Role.class));
                        user.getAuthorities().add(this.modelMapper
                                .map(roleService.findByAuthority("ROLE_CUSTOMER"), Role.class));
                        break;
                    case "ROLE_ADMIN":
                        user.getAuthorities().add(this.modelMapper
                                .map(roleService.findByAuthority("ROLE_USER"), Role.class));
                        user.getAuthorities().add(this.modelMapper
                                .map(roleService.findByAuthority("ROLE_MODERATOR"), Role.class));
                        user.getAuthorities().add(this.modelMapper
                                .map(roleService.findByAuthority("ROLE_ADMIN"), Role.class));
                        user.getAuthorities().add(this.modelMapper
                                .map(roleService.findByAuthority("ROLE_CUSTOMER"), Role.class));
                        break;
                }
            }else{
                switch (role.getAuthority()) {
                case "ROLE_USER":
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority("ROLE_USER"), Role.class));
                    break;
                case "ROLE_MODERATOR":
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority("ROLE_USER"), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority("ROLE_MODERATOR"), Role.class));
                    break;
                case "ROLE_ADMIN":
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority("ROLE_USER"), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority("ROLE_MODERATOR"), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority("ROLE_ADMIN"), Role.class));
                    break;
            }
            }
            this.userRepository.save(user);
        }
    }

    @Override
    public void addUserAnotherData(User user, String firstName, String lastName, LocalDate dateOfBirth, String gender, MultipartFile image) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal()
                , auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        user.getAuthorities().add(this.modelMapper.map(roleService.findByAuthority("ROLE_CUSTOMER"), Role.class));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dateOfBirth);
        user.setGender(Gender.valueOf(gender));

        setProfilePicture(image, user);

        this.userRepository.save(user);
    }

    @Override
    public void editUserProfile(UserServiceModel userServiceModel) {
        User user = userRepository.findUserByUsername(userServiceModel.getUsername())
                .orElse(null);
        if(user != null){
            user.setFirstName(userServiceModel.getFirstName());
            user.setLastName(userServiceModel.getLastName());
            this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
        }
    }

    @Override
    public void changeUserPassword(UserServiceModel userServiceModel, String oldPassword) {
        User user = userRepository.findUserByUsername(userServiceModel.getUsername())
                .orElse(null);
        if(user != null){
            if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
                throw new UserNotFoundException("Incorrect old password!");
            }
            user.setPassword(bCryptPasswordEncoder.encode(userServiceModel.getPassword()));
            user.setEmail(userServiceModel.getEmail());
            this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
        }
    }

    @Override
    public void updateUser(UserServiceModel userServiceModel) {
        User editedUser = this.modelMapper.map(userServiceModel, User.class);
        userRepository.save(editedUser);
    }

    @Override
    public void changeProfilePicture(UserServiceModel userServiceModel, MultipartFile image) throws IOException {
        User user = userRepository.findUserByUsername(userServiceModel.getUsername())
                .orElse(null);

        if(user != null) {
            setProfilePicture(image, user);
            this.userRepository.save(user);
        }
    }


    private void setProfilePicture(MultipartFile image, User user) throws IOException {
        if (image.isEmpty()) {
            Cloudinary cloudinary = new Cloudinary();
            String defaultImg;
            String saveDefImg;
            if(user.getGender().equals(Gender.MALE)){
                 defaultImg = cloudinary.url().cloudName("gladiators")
                        .imageTag("https://res.cloudinary.com/gladiators/image/upload/v1599468350/profile-pictures/profile_quok32_qsbjk2.jpg");
                 saveDefImg = defaultImg.substring(10, 115);
            }else{
                defaultImg = cloudinary.url().cloudName("gladiators")
                        .imageTag("https://res.cloudinary.com/gladiators/image/upload/v1599468317/profile-pictures/girlDefaultPic_o9foxm_uiss3t.jpg");
                saveDefImg = defaultImg.substring(10, 122);
            }
            user.setImageUrl(saveDefImg);
        } else {
            user.setImageUrl(this.cloudinaryService.uploadImage(image));
        }
    }

    @Override
    public void banUser(String id) {
        User user = this.userRepository.findById(id).orElse(null);
        user.getAuthorities().clear();
        RoleServiceModel role = this.roleService.findByAuthority("ROLE_BANNED");
        user.getAuthorities().add(this.modelMapper
                .map(role, Role.class));
        this.userRepository.save(user);

    }

    @Override
    public UserServiceModel findUserByUsername(String username) {

        return this.modelMapper.map(userRepository.findUserByUsername(username).get(),UserServiceModel.class);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findUserByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


}
