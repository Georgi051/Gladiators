package project.gladiators.service.impl;

import com.cloudinary.Cloudinary;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.constants.RoleConstants;
import project.gladiators.exceptions.UserNotFoundException;
import project.gladiators.model.bindingModels.RoleChangeBindingModel;
import project.gladiators.model.bindingModels.UserEditBindingModel;
import project.gladiators.model.bindingModels.UserRegisterBindingModel;
import project.gladiators.model.entities.Role;
import project.gladiators.model.entities.User;
import project.gladiators.model.entities.VerificationToken;
import project.gladiators.model.enums.Gender;
import project.gladiators.repository.UserRepository;
import project.gladiators.repository.VerificationTokenRepository;
import project.gladiators.service.*;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;
import project.gladiators.web.viewModels.UserViewModel;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

import static project.gladiators.constants.ExceptionMessages.USER_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final VerificationTokenRepository tokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, CloudinaryService cloudinaryService, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder, VerificationTokenRepository tokenRepository) {

        this.userRepository = userRepository;
        this.roleService = roleService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
    }

    //todo security context holder refresh

    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel) {

        if (this.userRepository.count() == 0) {
            userServiceModel.setAuthorities(new HashSet<>());
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority((RoleConstants.ROOT)));
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority((RoleConstants.ADMIN)));
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority((RoleConstants.MODERATOR)));
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority((RoleConstants.USER)));
        } else {
            userServiceModel.setAuthorities(new HashSet<>());
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority((RoleConstants.USER)));
        }

        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));
        user.setRegisteredOn(LocalDateTime.now());
        this.userRepository.saveAndFlush(user);
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        List<UserServiceModel> users = this.userRepository
                .findAll()
                .stream()
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toList());
        return users;
    }

    @Override
    public UserServiceModel findById(String id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        UserServiceModel userServiceModel = this.modelMapper.map
                (user, UserServiceModel.class);
        if (user.getDateOfBirth() != null) {
            int age = Period.between(user.getDateOfBirth(), LocalDate.now()).getYears();
            userServiceModel.setAge(age);
        } else {
            userServiceModel.setAge(0);
        }

        return this.modelMapper
                .map(userServiceModel, UserServiceModel.class);
    }

    @Override
    public void addRoleToUser(UserServiceModel userServiceModel, RoleServiceModel roleServiceModel) {
        User user = this.userRepository
                .findUserByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        boolean isCustomer = user.getAuthorities()
                .stream().anyMatch(role -> role.getAuthority().equals(RoleConstants.CUSTOMER));

        user.getAuthorities().clear();
        if (isCustomer) {
            switch (roleServiceModel.getAuthority()) {
                case RoleConstants.USER:
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.USER), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.CUSTOMER), Role.class));
                    break;
                case RoleConstants.MODERATOR:
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.USER), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.MODERATOR), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.CUSTOMER), Role.class));
                    break;
                case RoleConstants.ADMIN:
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.USER), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.ADMIN), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.CUSTOMER), Role.class));
                    break;
                case RoleConstants.ROLE_ADMIN_AND_MODERATOR:
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.USER), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.ADMIN), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.MODERATOR), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.CUSTOMER), Role.class));
                    break;
            }
        } else {
            switch (roleServiceModel.getAuthority()) {
                case RoleConstants.USER:
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.USER), Role.class));
                    break;
                case RoleConstants.MODERATOR:
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.USER), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.MODERATOR), Role.class));
                    break;
                case RoleConstants.ADMIN:
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.USER), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.ADMIN), Role.class));
                    break;
                case RoleConstants.ROLE_ADMIN_AND_MODERATOR:
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.USER), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.ADMIN), Role.class));
                    user.getAuthorities().add(this.modelMapper
                            .map(roleService.findByAuthority(RoleConstants.MODERATOR), Role.class));
                    break;
            }
        }
        this.userRepository.save(user);
    }

    @Override
    public void addUserAnotherData(User user, String firstName, String lastName, LocalDate dateOfBirth, String gender, MultipartFile image) throws IOException {
        Role customer = this.modelMapper.map(roleService.findByAuthority(RoleConstants.CUSTOMER), Role.class);

        sessionDynamicRoleChange(customer, null);
        user.getAuthorities().add(customer);
        setUserCredentials(user, firstName, lastName, dateOfBirth, gender, image);
        this.userRepository.save(user);
    }

    private void setUserCredentials(User user, String firstName, String lastName, LocalDate dateOfBirth, String gender, MultipartFile image) throws IOException {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dateOfBirth);
        user.setGender(Gender.valueOf(gender));

        setProfilePicture(image, user);
    }

    @Override
    public void editUserProfile(UserServiceModel userServiceModel) {
        User user = userRepository.findUserByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        user.setFirstName(userServiceModel.getFirstName());
        user.setLastName(userServiceModel.getLastName());
        this.userRepository.saveAndFlush(user);
        this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public void changeUserPassword(UserEditBindingModel userEditBindingModel) {
        User user = userRepository.findUserByUsername(userEditBindingModel.getUsername())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        user.setPassword(bCryptPasswordEncoder.encode(userEditBindingModel.getPassword()));
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void updateUser(UserServiceModel userServiceModel) {
        User editedUser = this.modelMapper.map(userServiceModel, User.class);
        userRepository.save(editedUser);
    }


    @Override
    public void confirmTrainer(String username, UserServiceModel userServiceModel, MultipartFile profilePicture) throws IOException {
        Role trainerConfirmed = this.modelMapper.map(roleService.findByAuthority(RoleConstants.TRAINER_CONFIRMED), Role.class);
        Role trainerUnconfirmed = this.modelMapper.map(roleService.findByAuthority(RoleConstants.TRAINER_UNCONFIRMED), Role.class);
        User user = this.userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

        sessionDynamicRoleChange(trainerConfirmed, trainerUnconfirmed);
        user.getAuthorities().add(trainerConfirmed);
        user.getAuthorities().remove(trainerUnconfirmed);

        setUserCredentials(user, userServiceModel.getFirstName(), userServiceModel.getLastName(), userServiceModel.getDateOfBirth(), userServiceModel.getGender().name(), profilePicture);
        userRepository.save(user);
    }

    @Override
    public void changeUserRole(String id, RoleChangeBindingModel roleChangeBindingModel) {
        UserServiceModel user = this.findById(id);
        RoleServiceModel role = new RoleServiceModel();
        role.setAuthority(roleChangeBindingModel.getRole());
        if (role.getAuthority() != null) {
            this.addRoleToUser(user, role);
        }

    }

    @Override
    public void saveRegisteredUser(User user) {
        this.userRepository.save(user);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(user, token);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    private void sessionDynamicRoleChange(Role addRole, Role removeRole) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        if (addRole != null && removeRole != null) {
            updatedAuthorities.add(addRole);
            updatedAuthorities.remove(removeRole);
        } else if (addRole != null) {
            updatedAuthorities.add(addRole);
        } else if (removeRole != null) {
            updatedAuthorities.remove(removeRole);
        }
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal()
                , auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Override
    public void changeProfilePicture(UserServiceModel userServiceModel, MultipartFile image) throws IOException {
        User user = userRepository.findUserByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        setProfilePicture(image, user);
        this.userRepository.save(user);
    }

    private void setProfilePicture(MultipartFile image, User user) throws IOException {
        if (image.isEmpty()) {
            Cloudinary cloudinary = new Cloudinary();
            String defaultImg;
            String saveDefImg;
            if (user.getGender().equals(Gender.MALE)) {
                defaultImg = cloudinary.url().cloudName("gladiators")
                        .imageTag("https://res.cloudinary.com/gladiators/image/upload/v1599468350/profile-pictures/profile_quok32_qsbjk2.jpg");
                saveDefImg = defaultImg.substring(10, 115);
            } else {
                defaultImg = cloudinary.url().cloudName("gladiators")
                        .imageTag("https://res.cloudinary.com/gladiators/image/upload/v1599468317/profile-pictures/girlDefaultPic_o9foxm_uiss3t.jpg");
                saveDefImg = defaultImg.substring(10, 122);
            }
            user.setImageUrl(saveDefImg);
        } else {
            user.setImageUrl(this.cloudinaryService.uploadImageToCurrentFolder(image, "profile-pictures"));
        }
    }

    @Override
    public void banUser(String id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        user.getAuthorities().clear();
        RoleServiceModel role = this.roleService.findByAuthority(RoleConstants.BANNED);
        user.getAuthorities().add(this.modelMapper
                .map(role, Role.class));
        this.userRepository.save(user);

    }

    @Override
    public UserServiceModel findUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findUserByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

}
