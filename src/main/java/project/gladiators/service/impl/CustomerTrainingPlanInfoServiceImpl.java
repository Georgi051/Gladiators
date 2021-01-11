package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.gladiators.model.entities.Customer;
import project.gladiators.model.entities.CustomerTrainingPlanInfo;
import project.gladiators.model.entities.TrainingPlan;
import project.gladiators.repository.CustomerRepository;
import project.gladiators.repository.CustomerTrainingPlanInfoRepository;
import project.gladiators.repository.TrainingPlanRepository;
import project.gladiators.service.CustomerTrainingPlanInfoService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.CustomerTrainingPlanInfoServiceModel;
import project.gladiators.service.serviceModels.TrainingPlanServiceModel;

@Service
public class CustomerTrainingPlanInfoServiceImpl implements CustomerTrainingPlanInfoService {

    private final CustomerTrainingPlanInfoRepository customerTrainingPlanInfoRepository;
    private final TrainingPlanRepository trainingPlanRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerTrainingPlanInfoServiceImpl(CustomerTrainingPlanInfoRepository customerTrainingPlanInfoRepository, TrainingPlanRepository trainingPlanRepository, CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerTrainingPlanInfoRepository = customerTrainingPlanInfoRepository;
        this.trainingPlanRepository = trainingPlanRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerTrainingPlanInfoServiceModel findByCustomer(CustomerServiceModel customer) {
        CustomerTrainingPlanInfo customerTrainingPlanInfo =
                this.customerTrainingPlanInfoRepository.findByCustomer_Id(customer.getId());
        if(customerTrainingPlanInfo != null){
            CustomerTrainingPlanInfoServiceModel customerTrainingPlanInfoServiceModel = this
                    .modelMapper.map(customerTrainingPlanInfo, CustomerTrainingPlanInfoServiceModel.class);
            customerTrainingPlanInfoServiceModel.setCustomerServiceModel(customer);
            Customer customer1 = this.customerRepository.findFirstById(customer.getId());
            TrainingPlan trainingPlan = this.trainingPlanRepository.getByCustomers(customer1);
            customerTrainingPlanInfoServiceModel.setTrainingPlanServiceModel(this.modelMapper
                    .map(trainingPlan, TrainingPlanServiceModel.class));
            return customerTrainingPlanInfoServiceModel;
        }else {
            return null;
        }

    }
}
