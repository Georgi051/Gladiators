package project.gladiators.integrational.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.constants.RoleConstants;
import project.gladiators.exceptions.UserNotFoundException;
import project.gladiators.model.bindingModels.RoleChangeBindingModel;
import project.gladiators.model.bindingModels.UserEditBindingModel;
import project.gladiators.model.entities.Role;
import project.gladiators.model.entities.User;
import project.gladiators.model.entities.VerificationToken;
import project.gladiators.model.enums.Gender;
import project.gladiators.repository.UserRepository;
import project.gladiators.repository.VerificationTokenRepository;
import project.gladiators.service.CloudinaryService;
import project.gladiators.service.RoleService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.gladiators.constants.ExceptionMessages.USER_NOT_FOUND;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private final String USER_NAME = "gosho";
    private final LocalDateTime REGISTERED_ON = LocalDateTime.now();
    private final String PASSWORD = "1234";
    private final String EMAIL = "gosho@gmail.com";

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    RoleService roleService;

    @Autowired
    CloudinaryService cloudinaryService;

    @MockBean
    VerificationTokenRepository tokenRepository;

    @MockBean
    UserRepository userRepository;

    private User user;
    private Role role;


    @BeforeEach
    void setUp() {
        user = new User(USER_NAME, null, null, REGISTERED_ON, PASSWORD, EMAIL,
                "https://res.cloudinary.com/gladiators/image/upload/v1599468350/profile-pictures/profile_quok32_qsbjk2.jpg",
                null, null, null, true);
        user.setId("1");

        Set<Role> roleSet = new HashSet<>();
        role = new Role("ROLE_USER");
        role.setId("1");
        roleSet.add(role);
        user.setAuthorities(roleSet);
    }

    @Test
    void testRegisterUserWithAllRolesShouldReturnCorrectResult() {
        UserServiceModel userServiceModel = this.userService.registerUser(this.modelMapper.map(user, UserServiceModel.class));

        Assertions.assertEquals("1", userServiceModel.getId());
        Assertions.assertEquals(USER_NAME, userServiceModel.getUsername());
        verify(userRepository).saveAndFlush(any());
    }

    @Test
    void testRegisterUserWithUserRoleOnlyShouldReturnCorrectResult() {
        user.setId("2");
        when(this.userRepository.count())
                .thenReturn(1L);

        UserServiceModel userServiceModel = this.userService.registerUser(this.modelMapper.map(user, UserServiceModel.class));

        Assertions.assertEquals("2", userServiceModel.getId());
        Assertions.assertEquals(USER_NAME, userServiceModel.getUsername());
        verify(userRepository).saveAndFlush(any());
    }

    @Test
    void testGetAllUsersShouldReturnCorrectResult() {
        when(this.userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserServiceModel> testAllUsers = this.userService.getAllUsers();
        UserServiceModel testUser = testAllUsers.get(0);
        Assertions.assertEquals(1, testAllUsers.size());
        Assertions.assertEquals("1", testUser.getId());
        Assertions.assertEquals(USER_NAME, testUser.getUsername());
    }

    @Test
    void testFindByIdShouldReturnCorrectResult() {
        when(this.userRepository.findById("1"))
                .thenReturn(Optional.of(user));

        UserServiceModel testUser = this.userService.findById("1");

        Assertions.assertEquals("1", testUser.getId());
        Assertions.assertEquals(USER_NAME, testUser.getUsername());
    }

    @Test
    void testFindByIdShouldThrowUserNotFoundException() {
        when(this.userRepository.findById("testId"))
                .thenReturn(null);

        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.findById("1");
        });
        Assertions.assertEquals(exception.getMessage(), USER_NOT_FOUND);
    }


    @Test
    void testAddUserRoleToUserWhichHasNoCustomerRole() {
        when(this.userRepository.findUserByUsername(USER_NAME))
                .thenReturn(Optional.of(user));

        UserServiceModel userServiceModel = this.modelMapper.map(user,UserServiceModel.class);
        RoleServiceModel roleServiceModel = new RoleServiceModel();
        roleServiceModel.setId("1");
        roleServiceModel.setAuthority(RoleConstants.USER);
        this.userService.addRoleToUser(userServiceModel,roleServiceModel);

        verify(this.userRepository).save(any());
    }


    @Test
    void testAddModeratorRoleToUserWhichHasNoCustomerRole() {
        when(this.userRepository.findUserByUsername(USER_NAME))
                .thenReturn(Optional.of(user));

        UserServiceModel userServiceModel = this.modelMapper.map(user,UserServiceModel.class);
        RoleServiceModel roleServiceModel = new RoleServiceModel();
        roleServiceModel.setId("1");
        roleServiceModel.setAuthority(RoleConstants.MODERATOR);
        this.userService.addRoleToUser(userServiceModel,roleServiceModel);

        verify(this.userRepository).save(any());
    }

    @Test
    void testAddAdminRoleToUserWhichHasNoCustomerRole() {
        when(this.userRepository.findUserByUsername(USER_NAME))
                .thenReturn(Optional.of(user));

        UserServiceModel userServiceModel = this.modelMapper.map(user,UserServiceModel.class);
        RoleServiceModel roleServiceModel = new RoleServiceModel();
        roleServiceModel.setId("1");
        roleServiceModel.setAuthority(RoleConstants.ADMIN);
        this.userService.addRoleToUser(userServiceModel,roleServiceModel);

        verify(this.userRepository).save(any());
    }

    @Test
    void testAddAdminAndModeratorRoleToUserWhichHasNoCustomerRole() {
        when(this.userRepository.findUserByUsername(USER_NAME))
                .thenReturn(Optional.of(user));

        UserServiceModel userServiceModel = this.modelMapper.map(user,UserServiceModel.class);
        RoleServiceModel roleServiceModel = new RoleServiceModel();
        roleServiceModel.setId("1");
        roleServiceModel.setAuthority(RoleConstants.ROLE_ADMIN_AND_MODERATOR);
        this.userService.addRoleToUser(userServiceModel,roleServiceModel);

        verify(this.userRepository).save(any());
    }


    @Test
    void testAddUserRoleToUserWhichHasCustomerRole() {
        Role userRole = new Role(RoleConstants.CUSTOMER);
        userRole.setId("2");
        user.getAuthorities().add(userRole);
        when(this.userRepository.findUserByUsername(USER_NAME))
                .thenReturn(Optional.of(user));

        UserServiceModel userServiceModel = this.modelMapper.map(user,UserServiceModel.class);
        RoleServiceModel roleServiceModel = new RoleServiceModel();
        roleServiceModel.setId("1");
        roleServiceModel.setAuthority(RoleConstants.USER);
        this.userService.addRoleToUser(userServiceModel,roleServiceModel);

        verify(this.userRepository).save(any());
    }

    @Test
    void testAddModeratorRoleToUserWhichHasCustomerRole() {
        Role userRole = new Role(RoleConstants.CUSTOMER);
        userRole.setId("2");
        user.getAuthorities().add(userRole);
        when(this.userRepository.findUserByUsername(USER_NAME))
                .thenReturn(Optional.of(user));

        UserServiceModel userServiceModel = this.modelMapper.map(user,UserServiceModel.class);
        RoleServiceModel roleServiceModel = new RoleServiceModel();
        roleServiceModel.setId("1");
        roleServiceModel.setAuthority(RoleConstants.MODERATOR);
        this.userService.addRoleToUser(userServiceModel,roleServiceModel);

        verify(this.userRepository).save(any());
    }

    @Test
    void testAddAdminRoleToUserWhichHasCustomerRole() {
        Role userRole = new Role(RoleConstants.CUSTOMER);
        userRole.setId("2");
        user.getAuthorities().add(userRole);
        when(this.userRepository.findUserByUsername(USER_NAME))
                .thenReturn(Optional.of(user));

        UserServiceModel userServiceModel = this.modelMapper.map(user,UserServiceModel.class);
        RoleServiceModel roleServiceModel = new RoleServiceModel();
        roleServiceModel.setId("1");
        roleServiceModel.setAuthority(RoleConstants.ADMIN);
        this.userService.addRoleToUser(userServiceModel,roleServiceModel);

        verify(this.userRepository).save(any());
    }

    @Test
    void testAddAdminAndModeratorRoleToUserWhichHasCustomerRole() {
        Role userRole = new Role(RoleConstants.CUSTOMER);
        userRole.setId("2");
        user.getAuthorities().add(userRole);
        when(this.userRepository.findUserByUsername(USER_NAME))
                .thenReturn(Optional.of(user));

        UserServiceModel userServiceModel = this.modelMapper.map(user,UserServiceModel.class);
        RoleServiceModel roleServiceModel = new RoleServiceModel();
        roleServiceModel.setId("1");
        roleServiceModel.setAuthority(RoleConstants.ROLE_ADMIN_AND_MODERATOR);
        this.userService.addRoleToUser(userServiceModel,roleServiceModel);

        verify(this.userRepository).save(any());
    }

    @Test
    void testEditUserProfileShouldSaveUserCorrect() {
        user.setFirstName("baseFirstName");
        user.setLastName("baseLastName");
        UserServiceModel testUserServiceModel = this.modelMapper.map(user, UserServiceModel.class);
        testUserServiceModel.setFirstName("testFirstName");
        testUserServiceModel.setLastName("testLastName");

        when(this.userRepository.findUserByUsername(USER_NAME))
                .thenReturn(Optional.of(user));

        this.userService.editUserProfile(testUserServiceModel);

        verify(userRepository).saveAndFlush(any());
    }

    @Test
    void testEditUserProfileShouldThrowUserNotFoundException() {
        user.setFirstName("baseFirstName");
        user.setLastName("baseLastName");
        UserServiceModel testUserServiceModel = this.modelMapper.map(user, UserServiceModel.class);
        testUserServiceModel.setFirstName("testFirstName");
        testUserServiceModel.setLastName("testLastName");

        when(this.userRepository.findUserByUsername("testUserName"))
                .thenReturn(null);

        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.editUserProfile(testUserServiceModel);
        });
        Assertions.assertEquals(exception.getMessage(), USER_NOT_FOUND);
    }

    @Test
    void testChangeUserPasswordShouldChangeSuccessfully() {
        when(this.userRepository.findUserByUsername(USER_NAME))
                .thenReturn(Optional.of(user));
        UserEditBindingModel testEditBindingModel = new UserEditBindingModel();
        testEditBindingModel.setUsername(USER_NAME);
        testEditBindingModel.setFirstName("baseFirstName");
        testEditBindingModel.setLastName("baseLastName");
        testEditBindingModel.setPassword("newPassWord1");
        testEditBindingModel.setConfirmPassword("newPassWord1");
        testEditBindingModel.setEmail(EMAIL);

        this.userService.changeUserPassword(testEditBindingModel);
        verify(userRepository).saveAndFlush(any());
    }


    @Test
    void testChangeUserRole() {
        when(this.userRepository.findById("1"))
                .thenReturn(Optional.of(user));

        when(this.userRepository.findUserByUsername(USER_NAME))
                .thenReturn(Optional.of(user));

        RoleChangeBindingModel roleServiceModel = new RoleChangeBindingModel();
        roleServiceModel.setId("1");
        roleServiceModel.setRole(RoleConstants.USER);
        this.userService.changeUserRole("1",roleServiceModel);

        verify(userRepository).save(any());
    }

    @Test
    void testSaveRegisteredUser() {
        this.userService.saveRegisteredUser(user);
        verify(userRepository).save(any());
    }

    @Test
    void testCreateVerificationToken() {
        String token = UUID.randomUUID().toString();
        this.userService.createVerificationToken(user, token);
        verify(tokenRepository).save(any());
    }

    @Test
    void testGetVerificationToken() {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(user, token);

        when(this.tokenRepository.findByToken(token))
                .thenReturn(verificationToken);

        VerificationToken testVerificationToken = this.userService.getVerificationToken(token);

        Assertions.assertEquals(verificationToken.getUser().getId(), testVerificationToken.getUser().getId());
        Assertions.assertEquals(verificationToken.getExpiryDate(), testVerificationToken.getExpiryDate());
        Assertions.assertEquals(verificationToken.getUser().getId(), testVerificationToken.getUser().getId());
    }

    @Test
    void testChangeProfilePictureToMaleDefault() throws IOException {
        user.setGender(Gender.MALE);
        when(this.userRepository.findUserByUsername(USER_NAME))
                .thenReturn(Optional.of(user));
        MultipartFile multipartFile = new MockMultipartFile("picture",
                null, IMAGE_JPEG.getMimeType(), (byte[]) null);

        this.userService.changeProfilePicture(this.modelMapper.map(user,UserServiceModel.class),multipartFile);
        verify(userRepository).save(any());
    }

    @Test
    void testChangeProfilePictureToFemaleDefault() throws IOException {
        user.setGender(Gender.FEMALE);
        when(this.userRepository.findUserByUsername(USER_NAME))
                .thenReturn(Optional.of(user));
        MultipartFile multipartFile = new MockMultipartFile("picture",
                null, IMAGE_JPEG.getMimeType(), (byte[]) null);

        this.userService.changeProfilePicture(this.modelMapper.map(user,UserServiceModel.class),multipartFile);
        verify(userRepository).save(any());
    }

    @Test
    void testChangeProfilePictureShouldThrowUserNotFoundException() {
        when(this.userRepository.findUserByUsername("testUserName"))
                .thenReturn(null);

        MultipartFile multipartFile = new MockMultipartFile("picture",
                null, IMAGE_JPEG.getMimeType(), (byte[]) null);

        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.changeProfilePicture(this.modelMapper.map(user,UserServiceModel.class),multipartFile);
        });
        Assertions.assertEquals(exception.getMessage(), USER_NOT_FOUND);
    }

    @Test
    void testBanUserShouldChangeRoleCorrectly() {
        Set<Role> roleSet = new HashSet<>();
        Role roleUser = new Role("ROLE_USER");
        roleUser.setId("1");
        roleSet.add(roleUser);
        user.setAuthorities(roleSet);

        when(this.userRepository.findById("1"))
                .thenReturn(Optional.of(user));

        this.userService.banUser("1");

        verify(userRepository).save(any());
    }


    @Test
    void testBanUserShouldThrowUserNotFoundException() {
        when(this.userRepository.findById("testId"))
                .thenReturn(null);

        Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.banUser("1");
        });
        Assertions.assertEquals(exception.getMessage(), USER_NOT_FOUND);
    }

    @Test
    void testLoadUserByUsername() {
        Exception exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            this.userService.loadUserByUsername("");
        });
        Assertions.assertEquals(exception.getMessage(), USER_NOT_FOUND);
    }
}