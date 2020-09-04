package project.gladiators.service;

import org.springframework.web.multipart.MultipartFile;
import project.gladiators.model.bindingModels.ProgressChartEditBindingModel;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.io.IOException;

public interface CustomerService {
    void registerCustomer(CustomerServiceModel customer, MultipartFile imageUrl) throws IOException;

    CustomerServiceModel findCustomerByUser(UserServiceModel user);

    CustomerServiceModel findCustomerById(String id);

    void editProgressChart(CustomerServiceModel customer, ProgressChartEditBindingModel progressChartEditBindingModel);
}
