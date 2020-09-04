package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.model.bindingModels.ProgressChartEditBindingModel;
import project.gladiators.model.entities.Customer;
import project.gladiators.model.entities.ProgressChart;
import project.gladiators.model.entities.User;
import project.gladiators.repository.CustomerRepository;
import project.gladiators.service.CustomerService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.io.IOException;
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
            customer.getProgressChart().setBMI(customer.getBMI());
            customer.getProgressChart().setProgressDate(LocalDate.now());
            customer.getProgressChart().setHeight(customer.getHeight());
            customer.getProgressChart().setWeight(customer.getWeight());
            this.userService.addUserAnotherData(user,customerServiceModel.getFirstName(),customerServiceModel.getLastName(),
            customerServiceModel.getAge(),customerServiceModel.getGender(),imageUrl);
            this.customerRepository.saveAndFlush(customer);
        }
    }

    @Override
    public CustomerServiceModel findCustomerByUser(UserServiceModel user) {
        CustomerServiceModel customer = this.modelMapper
        .map(this.customerRepository
                .findCustomerByUser(this.modelMapper
                .map(user, User.class)), CustomerServiceModel.class);
        return customer;

    }

    @Override
    public CustomerServiceModel findCustomerById(String id) {
        return this.modelMapper
                .map(this.customerRepository.findById(id), CustomerServiceModel.class);
    }

    @Override
    public void editProgressChart(CustomerServiceModel customer, ProgressChartEditBindingModel progressChartEditBindingModel) {

        Customer customerEntity = this.customerRepository.findById(customer.getId())
                .orElse(null);
        customerEntity.getProgressChart().setWeight(progressChartEditBindingModel.getWeight());
        customerEntity.getProgressChart().setHeight(progressChartEditBindingModel.getHeight());
        customerEntity.getProgressChart().setBMI(progressChartEditBindingModel.getBMI());
        customerEntity.getProgressChart().setBiceps(progressChartEditBindingModel.getBiceps());
        customerEntity.getProgressChart().setChest(progressChartEditBindingModel.getChest());
        customerEntity.getProgressChart().setWaist(progressChartEditBindingModel.getWaist());
        customerEntity.getProgressChart().setThigh(progressChartEditBindingModel.getThigh());
        this.customerRepository.saveAndFlush(customerEntity);
    }
}
