package project.gladiators.service;

import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.CustomerTrainingPlanInfoServiceModel;

public interface CustomerTrainingPlanInfoService {

    CustomerTrainingPlanInfoServiceModel findByCustomer(CustomerServiceModel customer);
}
