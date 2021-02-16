package project.gladiators.integrational.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.gladiators.model.entities.Customer;
import project.gladiators.model.entities.CustomerTrainingPlanInfo;
import project.gladiators.model.entities.TrainingPlan;
import project.gladiators.model.enums.TrainingPlanType;
import project.gladiators.repository.CustomerRepository;
import project.gladiators.repository.CustomerTrainingPlanInfoRepository;
import project.gladiators.repository.TrainingPlanRepository;
import project.gladiators.service.CustomerTrainingPlanInfoService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.CustomerTrainingPlanInfoServiceModel;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CustomerTrainingPlanInfoServiceImplTest {

    Customer customer;
    TrainingPlan trainingPlan;
    CustomerTrainingPlanInfo customerTrainingPlanInfo;

    @MockBean
    CustomerTrainingPlanInfoRepository customerTrainingPlanInfoRepository;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    TrainingPlanRepository trainingPlanRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CustomerTrainingPlanInfoService customerTrainingPlanInfoService;

    @BeforeEach
    public void setUp(){
        customer = new Customer();
        customer.setId("1");
        trainingPlan = new TrainingPlan();
        trainingPlan.setTrainingPlanType(TrainingPlanType.LOST_WEIGHT);
        trainingPlan.setCustomers(new HashSet<>());
        trainingPlan.getCustomers().add(customer);
        customerTrainingPlanInfo = new CustomerTrainingPlanInfo();
        customerTrainingPlanInfo.setCustomer(customer);
        customerTrainingPlanInfo.setTrainingPlan(trainingPlan);
    }

    @Test
    public void findByCustomer_shouldReturnCorrectTrainingPlan(){

        when(customerRepository.findFirstById("1"))
                .thenReturn(customer);
        when(trainingPlanRepository.getByCustomers(customer))
                .thenReturn(trainingPlan);
        when(customerTrainingPlanInfoRepository.findByCustomer_Id("1"))
                .thenReturn(Optional.of(customerTrainingPlanInfo));

        CustomerTrainingPlanInfoServiceModel customerTrainingPlanInfoServiceModel =
                customerTrainingPlanInfoService.findByCustomer(modelMapper.map(customer, CustomerServiceModel.class));

        assertEquals("1", customerTrainingPlanInfoServiceModel.getCustomerServiceModel().getId());
    }

    @Test
    public void findByCustomer_shouldReturnNullIfThereArentSuchCustomerWithTrainingPlan(){
        when(customerRepository.findFirstById("1"))
                .thenReturn(customer);
        when(trainingPlanRepository.getByCustomers(customer))
                .thenReturn(trainingPlan);
        when(customerTrainingPlanInfoRepository.findByCustomer_Id("1"))
                .thenReturn(Optional.of(customerTrainingPlanInfo));

        CustomerTrainingPlanInfoServiceModel customerTrainingPlanInfoServiceModel =
                customerTrainingPlanInfoService.findByCustomer(new CustomerServiceModel());

        assertNull(customerTrainingPlanInfoServiceModel);
    }

    @Test
    public void customerPaidTrainingPlan_shouldChangePaidToTrue(){
        when(customerRepository.findFirstById("1"))
                .thenReturn(customer);
        when(trainingPlanRepository.getByCustomers(customer))
                .thenReturn(trainingPlan);
        when(customerTrainingPlanInfoRepository.findByCustomer_Id("1"))
                .thenReturn(Optional.of(customerTrainingPlanInfo));

        customerTrainingPlanInfoService.customerPaidTrainingPlan(modelMapper.map(customer, CustomerServiceModel.class));

        CustomerTrainingPlanInfoServiceModel customerTrainingPlanInfoServiceModel = customerTrainingPlanInfoService.findByCustomer
                (modelMapper.map(customer, CustomerServiceModel.class));
        assertTrue(customerTrainingPlanInfoServiceModel.isPaid());

    }
}
