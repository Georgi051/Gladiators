package project.gladiators.service;

import org.springframework.web.multipart.MultipartFile;
import project.gladiators.model.bindingModels.ProgressChartEditBindingModel;
import project.gladiators.model.entities.User;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.ProgressChartServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.io.IOException;
import java.util.List;

public interface CustomerService {
    void registerCustomer(CustomerServiceModel customer, MultipartFile imageUrl, User user) throws IOException;

    CustomerServiceModel findCustomerByUser(UserServiceModel user);

    CustomerServiceModel findCustomerById(String id);

    void editProgressChart(CustomerServiceModel customer, ProgressChartServiceModel progressChartServiceModel);

    List<CustomerServiceModel> findAll();
}
