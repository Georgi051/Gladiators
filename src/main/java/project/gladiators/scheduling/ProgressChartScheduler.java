package project.gladiators.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.gladiators.model.entities.Customer;
import project.gladiators.model.entities.ProgressChart;
import project.gladiators.repository.CustomerRepository;
import project.gladiators.repository.ProgressChartRepository;
import project.gladiators.service.CustomerService;
import project.gladiators.service.UserService;

import java.util.List;

@Component
public class ProgressChartScheduler {
    private final CustomerRepository customerRepository;
    private final ProgressChartRepository progressChartRepository;

    public ProgressChartScheduler(CustomerRepository customerRepository, ProgressChartRepository progressChartRepository) {
        this.customerRepository = customerRepository;
        this.progressChartRepository = progressChartRepository;
    }

    @Scheduled(cron = "0 0 0 8-14,22-28 * 7")
    public void changeProgressChartStatus(){
        List<Customer> customers = this.customerRepository.findAll();

        customers.forEach(customer -> {
            ProgressChart progressChart = customer.getProgressChart();
            progressChart.setChanged(false);
            this.progressChartRepository.saveAndFlush(progressChart);
            customer.setProgressChart(progressChart);
            this.customerRepository.save(customer);
        });

    }

}
