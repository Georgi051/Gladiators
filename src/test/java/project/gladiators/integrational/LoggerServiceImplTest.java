package project.gladiators.integrational;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.gladiators.model.entities.Logger;
import project.gladiators.model.entities.Role;
import project.gladiators.model.entities.User;
import project.gladiators.repository.LoggerRepository;
import project.gladiators.service.LoggerService;
import project.gladiators.service.serviceModels.AdminLogServiceModel;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.gladiators.constants.RoleConstants.ADMIN;
import static project.gladiators.constants.RoleConstants.CUSTOMER;
import static project.gladiators.model.enums.Action.BAN;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class LoggerServiceImplTest {
    private final String ADMIN_ID = "1";
    private final String ADMIN_NAME = "Ivan";
    private final String ADMIN_EMAIL = "Ivan@gmail.com";
    private final String USER_ID = "2";
    private final String USER_NAME = "Pesho";
    private final String USER_EMAIL = "Pesho@gmail.com";
    private final String DESCRIPTION = "Ban reason";

    @Autowired
    LoggerService loggerService;

    @MockBean
    LoggerRepository loggerRepository;

    @Autowired
    ModelMapper modelMapper;

    private Logger logger;
    private UserServiceModel user;
    private UserServiceModel admin;

    @BeforeEach
    public void setUp() {
        logger = new Logger();

        admin = new UserServiceModel();
        admin.setId(ADMIN_ID);
        admin.setUsername(ADMIN_NAME);
        admin.setEmail(ADMIN_EMAIL);
        admin.setAuthorities(Set.of(modelMapper.map(new Role(ADMIN), RoleServiceModel.class)));

        user = new UserServiceModel();
        user.setId(USER_ID);
        user.setUsername(USER_NAME);
        user.setEmail(USER_EMAIL);
        user.setAuthorities(Set.of(modelMapper.map(new Role(CUSTOMER), RoleServiceModel.class)));

        logger.setMadeOn(LocalDateTime.now());
        logger.setDescription(DESCRIPTION);
        logger.setAction(BAN);
        logger.setUser(this.modelMapper.map(user, User.class));
        logger.setAdmin(this.modelMapper.map(admin, User.class));
    }

    @Test
    void testLogShouldSaveDataCollect() {
        this.loggerService.log(BAN, DESCRIPTION, LocalDateTime.now(), admin, user);

        verify(loggerRepository).save(any());
    }

    @Test
    void testFindAllLogsShouldReturnCollection() {
        when(this.loggerRepository.findAll())
                .thenReturn(List.of(logger));

        List<AdminLogServiceModel> adminLogServiceModelList = this.loggerService.findAll();
        AdminLogServiceModel logServiceModel = adminLogServiceModelList.get(0);

        Assertions.assertEquals(1, adminLogServiceModelList.size());
        Assertions.assertEquals(logger.getUser().getUsername(), logServiceModel.getUser().getUsername());
        Assertions.assertEquals(logger.getAdmin().getUsername(), logServiceModel.getAdmin().getUsername());
    }
}