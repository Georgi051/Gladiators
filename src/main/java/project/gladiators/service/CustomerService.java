package project.gladiators.service;

import org.springframework.web.multipart.MultipartFile;
import project.gladiators.service.serviceModels.CustomerServiceModel;

import java.io.IOException;

public interface CustomerService {
    void registerCustomer(CustomerServiceModel customer, MultipartFile imageUrl) throws IOException;
}
