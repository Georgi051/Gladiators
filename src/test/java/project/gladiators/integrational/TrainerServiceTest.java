package project.gladiators.integrational;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.constants.RoleConstants;
import project.gladiators.exceptions.InvalidChangeTrainerStatusException;
import project.gladiators.exceptions.TrainerNotFoundException;
import project.gladiators.model.entities.Role;
import project.gladiators.model.entities.Trainer;
import project.gladiators.model.entities.User;
import project.gladiators.model.enums.Action;
import project.gladiators.model.enums.Gender;
import project.gladiators.repository.RoleRepository;
import project.gladiators.repository.TrainerRepository;
import project.gladiators.repository.UserRepository;
import project.gladiators.service.RoleService;
import project.gladiators.service.TrainerService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.TrainerServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static project.gladiators.constants.RoleConstants.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    Trainer trainer;
    User user;
    Role roleUser;
    Role roleTrainerConfirmed;
    Role roleTrainerUnconfirmed;
    MultipartFile multipartFile;

    @MockBean
    TrainerRepository trainerRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    Authentication authentication;

    @MockBean
    SecurityContext securityContext;

    @Autowired
    UserService userService;

    @Autowired
    TrainerService trainerService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RoleService roleService;

    @BeforeEach
    public void setUp(){
        trainer = new Trainer();
        trainer.setId("1");
        trainer.setDescription("Test Description");
        trainer.setYearsOfExperience(10);
        user = new User();
        user.setUsername("Trainer");
        user.setId("1");
        user.setAuthorities(new HashSet<>());
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setDateOfBirth(LocalDate.now());
        user.setGender(Gender.MALE);
        roleUser = new Role();
        roleUser.setId("1");
        roleUser.setAuthority(USER);
        user.getAuthorities().add(roleUser);
        trainer.setUser(user);
        roleTrainerConfirmed = new Role();
        roleTrainerConfirmed.setId("2");
        roleTrainerConfirmed.setAuthority(TRAINER_CONFIRMED);
        roleTrainerUnconfirmed = new Role();
        roleTrainerUnconfirmed.setId("3");
        roleTrainerUnconfirmed.setAuthority(TRAINER_UNCONFIRMED);
        multipartFile = new MockMultipartFile("picture",
                "https://res.cloudinary.com/gladiators/image/upload/v1599061356/No-image-found_vtfx1x.jpg",
                IMAGE_JPEG.getMimeType(), (byte[]) null);
    }

    @Test
    public void changeTrainerStatus_shouldCreateTrainerIfActionIsCreate(){
        when(userRepository.findUserByUsername("Trainer"))
                .thenReturn(Optional.of(user));
        when(roleRepository.findByAuthority(USER))
                .thenReturn(roleUser);
        when(roleRepository.findByAuthority(TRAINER_CONFIRMED))
                .thenReturn(roleTrainerConfirmed);
        when(roleRepository.findByAuthority(TRAINER_UNCONFIRMED))
                .thenReturn(roleTrainerUnconfirmed);
        when(userRepository.findUserByUsername("Trainer"))
                .thenReturn(Optional.of(user));

        trainerService.changeTrainerStatus("Trainer", Action.CREATE);
        verify(trainerRepository).save(any(Trainer.class));

    }

    @Test
    public void changeTrainerStatus_shouldThrowExceptionIfTheUserHasAlreadyTrainerRole(){
        user.getAuthorities().add(roleTrainerConfirmed);
        when(userRepository.findUserByUsername("Trainer"))
                .thenReturn(Optional.of(user));
        when(roleRepository.findByAuthority(USER))
                .thenReturn(roleUser);
        when(roleRepository.findByAuthority(TRAINER_CONFIRMED))
                .thenReturn(roleTrainerConfirmed);
        when(roleRepository.findByAuthority(TRAINER_UNCONFIRMED))
                .thenReturn(roleTrainerUnconfirmed);
        when(userRepository.findUserByUsername("Trainer"))
                .thenReturn(Optional.of(user));

        assertThrows(InvalidChangeTrainerStatusException.class, () ->
                trainerService.changeTrainerStatus("Trainer", Action.CREATE));
    }

    @Test
    public void changeTrainerStatus_shouldDeleteTrainerIfActionIsDelete(){
        user.getAuthorities().add(roleTrainerConfirmed);
        when(userRepository.findUserByUsername("Trainer"))
                .thenReturn(Optional.of(user));
        when(roleRepository.findByAuthority(USER))
                .thenReturn(roleUser);
        when(roleRepository.findByAuthority(TRAINER_CONFIRMED))
                .thenReturn(roleTrainerConfirmed);
        when(roleRepository.findByAuthority(TRAINER_UNCONFIRMED))
                .thenReturn(roleTrainerUnconfirmed);
        when(userRepository.findUserByUsername("Trainer"))
                .thenReturn(Optional.of(user));
        trainerService.changeTrainerStatus("Trainer", Action.DELETE);
        verify(trainerRepository).deleteTrainerByUser_Id("1");

    }

    @Test
    public void changeTrainerStatus_shouldThrowExceptionIfTheUserIsNotTrainerAndYouTryToDeleteIt(){
        when(userRepository.findUserByUsername("Trainer"))
                .thenReturn(Optional.of(user));
        when(roleRepository.findByAuthority(USER))
                .thenReturn(roleUser);
        when(roleRepository.findByAuthority(TRAINER_CONFIRMED))
                .thenReturn(roleTrainerConfirmed);
        when(roleRepository.findByAuthority(TRAINER_UNCONFIRMED))
                .thenReturn(roleTrainerUnconfirmed);
        when(userRepository.findUserByUsername("Trainer"))
                .thenReturn(Optional.of(user));
        assertThrows(InvalidChangeTrainerStatusException.class, () ->
                trainerService.changeTrainerStatus("Trainer", Action.DELETE));
    }

    @Test
    public void confirmTrainer_shouldChangeUnconfirmedToConfirmTrainer() throws IOException {
        user.getAuthorities().add(roleTrainerUnconfirmed);
        when(roleRepository.findByAuthority(USER))
                .thenReturn(roleUser);
        when(roleRepository.findByAuthority(TRAINER_CONFIRMED))
                .thenReturn(roleTrainerConfirmed);
        when(roleRepository.findByAuthority(TRAINER_UNCONFIRMED))
                .thenReturn(roleTrainerUnconfirmed);
        when(trainerRepository.findTrainerByUser_Username("Trainer"))
                .thenReturn(Optional.of(trainer));
        when(userRepository.findUserByUsername("Trainer"))
                .thenReturn(Optional.of(user));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        TrainerServiceModel trainerServiceModel = modelMapper.map(trainer, TrainerServiceModel.class);
        UserServiceModel userServiceModel = modelMapper.map(user, UserServiceModel.class);
        trainerService.confirmTrainer(trainerServiceModel, userServiceModel, "Trainer", multipartFile);

        User testUser = userRepository.findUserByUsername("Trainer").orElse(null);
        assertTrue(testUser.getAuthorities().contains(roleTrainerConfirmed));
    }

    @Test
    public void findAll_shouldReturnAllConfirmedTrainers() throws IOException {
        trainer.getUser().getAuthorities().add(roleTrainerConfirmed);
        List<Trainer> trainers = new ArrayList<>(List.of(trainer));
        when(trainerRepository.findAll()).thenReturn(trainers);
        List<TrainerServiceModel> trainerServiceModels = trainerService.findAll();
        assertEquals(1, trainerServiceModels.size());

    }

    @Test
    public void findAll_shouldThrowExceptionIfThereArentTrainers(){
        assertThrows(TrainerNotFoundException.class, () -> trainerService.findAll());
    }

    @Test
    public void findById_shouldReturnTrainerById(){
        when(trainerRepository.findById("1"))
                .thenReturn(Optional.of(trainer));

        TrainerServiceModel trainerServiceModel = trainerService.findById("1");

        assertEquals("Trainer", trainerServiceModel.getUser().getUsername());

    }

    @Test
    public void findByUserName_shouldReturnTrainerByUsername(){
        when(trainerRepository.findTrainerByUser_Username("Trainer"))
                .thenReturn(Optional.of(trainer));

        TrainerServiceModel trainerServiceModel = trainerService.findByUserName("Trainer");

        assertEquals("1", trainerServiceModel.getId());
    }

}
