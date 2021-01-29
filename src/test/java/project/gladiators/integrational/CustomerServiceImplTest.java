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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.constants.RoleConstants;
import project.gladiators.exceptions.CustomerNotFoundException;
import project.gladiators.init.RolesInitialization;
import project.gladiators.model.entities.Customer;
import project.gladiators.model.entities.ProgressChart;
import project.gladiators.model.entities.Role;
import project.gladiators.model.entities.User;
import project.gladiators.repository.CustomerRepository;
import project.gladiators.repository.ProgressChartRepository;
import project.gladiators.repository.RoleRepository;
import project.gladiators.repository.UserRepository;
import project.gladiators.service.CustomerService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.ProgressChartServiceModel;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.gladiators.constants.ExceptionMessages.CUSTOMER_NOT_FOUND;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    private final String USER_NAME = "gosho";
    private final String FIRST_NAME = "Georgi";
    private final String LAST_NAME = "Ivanov";
    private final LocalDateTime REGISTERED_ON = LocalDateTime.now();
    private final String PASSWORD = "1234";
    private final String EMAIL = "gosho@gmail.com";
    private final LocalDate DATE_BIRTH = LocalDate.of(1992, 10, 3);


    @Autowired
    CustomerService customerService;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    ProgressChartRepository progressChartRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    Authentication authentication;

    @MockBean
    SecurityContext securityContext;

    @MockBean
    RolesInitialization rolesInitialization;

    @MockBean
    MultipartFile multipartFile;

    private CustomerServiceModel customerServiceModel;
    private UserServiceModel userServiceModel;
    private ProgressChartServiceModel progressChartServiceModel;
    private User user;
    private Customer customer;
    private Role roleUser;
    private Role roleCustomer;
    private RoleServiceModel roleServiceModel;

    @BeforeEach
    public void setUp() {
        user = new User(USER_NAME, null, null, REGISTERED_ON, PASSWORD, EMAIL,
                "https://res.cloudinary.com/gladiators/image/upload/v1599468350/profile-pictures/profile_quok32_qsbjk2.jpg",
                null, null, null, true);
        Set<Role> roleSet = new HashSet<>();
        roleUser = new Role("ROLE_USER");
        roleUser.setId("1");
        roleSet.add(roleUser);
        user.setAuthorities(roleSet);
        user.setId("1");

        userServiceModel = this.modelMapper.map(user, UserServiceModel.class);

        customerServiceModel = new CustomerServiceModel();
        customerServiceModel.setId("1");
        customerServiceModel.setFirstName(FIRST_NAME);
        customerServiceModel.setLastName(LAST_NAME);
        customerServiceModel.setGender("MALE");
        customerServiceModel.setWeight(74);
        customerServiceModel.setHeight(177);
        customerServiceModel.setDateOfBirth(DATE_BIRTH);
        customerServiceModel.setImageUrl("https://res.cloudinary.com/gladiators/image/upload/v1599468350/profile-pictures/profile_quok32_qsbjk2.jpg");

        customer = this.modelMapper.map(customerServiceModel, Customer.class);

        roleCustomer = new Role(RoleConstants.CUSTOMER);
        roleCustomer.setId("2");

        progressChartServiceModel = new ProgressChartServiceModel();
        progressChartServiceModel.setBiceps(40);
        progressChartServiceModel.setChest(88);
        progressChartServiceModel.setHeight(170);
        progressChartServiceModel.setWeight(77);
        progressChartServiceModel.setWaist(70);
        progressChartServiceModel.setThigh(44);
    }


    @Test
    void testRegisterCustomerShouldReturnCorrectResult() throws IOException {
        roleServiceModel = this.modelMapper.map(roleCustomer, RoleServiceModel.class);
        roleServiceModel.setId("2");

        when(this.roleRepository.findByAuthority("ROLE_CUSTOMER"))
                .thenReturn(roleCustomer);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MultipartFile multipartFile = new MockMultipartFile("picture",
                "https://res.cloudinary.com/gladiators/image/upload/v1599468350/profile-pictures/profile_quok32_qsbjk2.jpg"
                , IMAGE_JPEG.getMimeType(), (byte[]) null);

        customerService.registerCustomer(customerServiceModel, multipartFile, user);

        verify(customerRepository).saveAndFlush(any());
    }

    @Test
    void testFindCustomerByUserShouldReturnCorrectResult() {
        customerServiceModel.setUser(userServiceModel);

        when(this.customerRepository.findCustomerByUser(this.modelMapper.map(userServiceModel, User.class)))
                .thenReturn(this.modelMapper.map(customerServiceModel, Customer.class));

        CustomerServiceModel testCustomer = this.customerService.findCustomerByUser(userServiceModel);

        Assertions.assertEquals("1", testCustomer.getUser().getId());
        Assertions.assertEquals(USER_NAME, testCustomer.getUser().getUsername());
        Assertions.assertEquals(EMAIL, testCustomer.getUser().getEmail());
    }

    @Test
    void testFindCustomerByUserShouldReturnNullResult() {
        CustomerServiceModel testCustomer = this.customerService.findCustomerByUser(userServiceModel);
        Assertions.assertNull(testCustomer);
    }

    @Test
    void testFindCustomerByIdWithAddedAgeShouldReturn() {
        user.setDateOfBirth(DATE_BIRTH);
        customer.setUser(user);

        when(this.customerRepository.findFirstById("1"))
                .thenReturn(customer);

        CustomerServiceModel testCustomerById = this.customerService.findCustomerById("1");

        Assertions.assertEquals("1", testCustomerById.getUser().getId());
        Assertions.assertEquals(USER_NAME, testCustomerById.getUser().getUsername());
        Assertions.assertEquals(DATE_BIRTH, testCustomerById.getUser().getDateOfBirth());
        Assertions.assertEquals(Period.between(DATE_BIRTH, LocalDate.now()).getYears(), testCustomerById.getUser().getAge());
    }

    @Test
    void testFindCustomerByIdWithNoAgeAddedShouldReturnZeroAgeValue() {
        user.setDateOfBirth(null);
        customer.setUser(user);
        when(this.customerRepository.findFirstById("1"))
                .thenReturn(customer);

        CustomerServiceModel testCustomerById = this.customerService.findCustomerById("1");

        Assertions.assertEquals("1", testCustomerById.getUser().getId());
        Assertions.assertEquals(USER_NAME, testCustomerById.getUser().getUsername());
        Assertions.assertEquals(0, testCustomerById.getUser().getAge());
    }

    @Test
    void testEditProgressChartWithFoundCustomer() {
        ProgressChart progressChart = new ProgressChart();
        progressChart.setWeight(70);
        progressChart.setChest(80);
        progressChart.setHeight(170);
        progressChart.setBiceps(35);
        progressChart.setWaist(72);
        progressChart.setThigh(40);
        customer.setProgressChart(progressChart);

        when(this.customerRepository.findById("1"))
                .thenReturn(Optional.of(customer));

        this.customerService.editProgressChart(customerServiceModel, progressChartServiceModel);

        verify(progressChartRepository).saveAndFlush(any());
        verify(customerRepository).saveAndFlush(any());
    }

    @Test
    void testEditProgressChartWithNoCustomerShouldThrowCustomerNotFoundException() {
        when(this.customerRepository.findById("testId"))
                .thenReturn(null);
        Exception exception = Assertions.assertThrows(CustomerNotFoundException.class, () -> {
            this.customerService.editProgressChart(customerServiceModel, progressChartServiceModel);
        });
        Assertions.assertEquals(exception.getMessage(), CUSTOMER_NOT_FOUND);
    }

    @Test
    void testFindAllShouldReturnCorrectResult() {
        customer.setUser(user);
        when(this.customerRepository.findAll())
                .thenReturn(List.of(customer));

        List<CustomerServiceModel> allCustomersList = this.customerService.findAll();
        CustomerServiceModel testCustomer = allCustomersList.get(0);

        Assertions.assertEquals(1, allCustomersList.size());
        Assertions.assertEquals("1", testCustomer.getId());
        Assertions.assertEquals(USER_NAME, testCustomer.getUser().getUsername());

    }
}