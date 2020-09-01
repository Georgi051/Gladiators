package project.gladiators.service;

import project.gladiators.model.entities.User;
import project.gladiators.service.serviceModels.CustomerServiceModel;

public interface CustomerService {
    void registerCustomer(CustomerServiceModel customer, User id);
}
