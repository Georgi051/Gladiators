package project.gladiators.integrational;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.gladiators.model.entities.Role;
import project.gladiators.repository.RoleRepository;
import project.gladiators.service.RoleService;
import project.gladiators.service.serviceModels.RoleServiceModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    List<Role> roles;
    Role admin;
    Role user;
    Role customer;

    @MockBean
    RoleRepository roleRepository;

    @Autowired
    RoleService roleService;

    @BeforeEach
    public void setUp(){
        admin = new Role();
        admin.setId("1");
        admin.setAuthority("ROLE_ADMIN");
        user = new Role();
        user.setId("2");
        user.setAuthority("ROLE_USER");
        customer = new Role();
        customer.setId("3");
        customer.setAuthority("ROLE_CUSTOMER");

        roles = new ArrayList<>(List.of(admin, user, customer));
    }

    @Test
    public void findAllRoles_shouldReturnAllRoles(){
        when(roleRepository.findAll())
                .thenReturn(roles);

        Set<RoleServiceModel> roleServiceModels = roleService.findAllRoles();

        assertEquals(3, roleServiceModels.size());
    }

    @Test
    public void findByAuthority_shouldReturnRoleByGivenAuthority(){
        when(roleRepository.findByAuthority("ROLE_ADMIN"))
                .thenReturn(admin);

        RoleServiceModel roleServiceModel = roleService.findByAuthority("ROLE_ADMIN");

        assertEquals("1", roleServiceModel.getId());
    }
}
