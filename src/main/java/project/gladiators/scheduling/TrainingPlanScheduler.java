package project.gladiators.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.gladiators.model.entities.Customer;
import project.gladiators.model.entities.CustomerTrainingPlanInfo;
import project.gladiators.repository.CustomerRepository;
import project.gladiators.repository.CustomerTrainingPlanInfoRepository;

import java.time.LocalDate;
import java.util.List;

@Component
public class TrainingPlanScheduler {

    private final CustomerRepository customerRepository;
    private final CustomerTrainingPlanInfoRepository customerTrainingPlanInfoRepository;

    public TrainingPlanScheduler(CustomerRepository customerRepository, CustomerTrainingPlanInfoRepository customerTrainingPlanInfoRepository) {
        this.customerRepository = customerRepository;
        this.customerTrainingPlanInfoRepository = customerTrainingPlanInfoRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void removeExpiredTrainingPlans(){
        List<Customer> customers = this.customerRepository.findAll();

        customers.forEach(customer -> {
            CustomerTrainingPlanInfo customerTrainingPlanInfo = this.customerTrainingPlanInfoRepository
                    .findByCustomer_Id(customer.getId());
            if(customerTrainingPlanInfo.getStartedOn().plusDays(28).isAfter(LocalDate.now())){
                this.customerTrainingPlanInfoRepository.delete(customerTrainingPlanInfo);
            }
        });

    }
}
