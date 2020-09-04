package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.model.entities.Customer;
import project.gladiators.model.entities.ProgressChart;
import project.gladiators.model.entities.User;
import project.gladiators.repository.CustomerRepository;
import project.gladiators.service.CustomerService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.CustomerServiceModel;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, UserService userService, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void registerCustomer(CustomerServiceModel customerServiceModel, MultipartFile imageUrl) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (this.customerRepository.findCustomerByUser(user) == null){
            Customer customer = this.modelMapper.map(customerServiceModel,Customer.class);
            customer.setUser(user);
            customer.setProgressChart(new ProgressChart());
            customer.getProgressChart().setProgressDate(LocalDate.now());
            customer.getProgressChart().setHeight(customer.getHeight());
            customer.getProgressChart().setWeight(customer.getWeight());
            double bmi = customer.getWeight()/Math.pow(customer.getHeight()/100,2);
            customer.getProgressChart().setBMI(bmi);
            customer.setBMI(bmi);
            this.userService.addUserAnotherData(user,customerServiceModel.getFirstName(),customerServiceModel.getLastName(),
            customerServiceModel.getAge(),customerServiceModel.getGender(),imageUrl);
            this.customerRepository.saveAndFlush(customer);
        }
    }
}
