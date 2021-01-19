package project.gladiators.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.model.entities.ProgressChart;
import project.gladiators.service.*;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.CustomerTrainingPlanInfoServiceModel;
import project.gladiators.service.serviceModels.TrainingPlanServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.time.LocalDate;
import java.time.Period;

@Service
public class HomeServiceImpl implements HomeService {
    private final UserService userService;
    private final CustomerService customerService;
    private final TrainingPlanService trainingPlanService;
    private final CustomerTrainingPlanInfoService customerTrainingPlanInfoService;
    private final MessageService messageService;
    private final static int TRAINING_PLAN_DURATION_DAYS = 28;

    @Autowired
    public HomeServiceImpl(UserService userService, CustomerService customerService, TrainingPlanService trainingPlanService, CustomerTrainingPlanInfoService customerTrainingPlanInfoService, MessageService messageService) {
        this.userService = userService;
        this.customerService = customerService;
        this.trainingPlanService = trainingPlanService;
        this.customerTrainingPlanInfoService = customerTrainingPlanInfoService;
        this.messageService = messageService;
    }

    public ModelAndView mvcService(String name, ModelAndView modelAndView) {
        UserServiceModel user = this.userService.findUserByUsername(name);
        CustomerServiceModel customer = this.customerService.findCustomerByUser(user);
        if(messageService.getSortedMessagesByUserId(user.getId()) != null){
            messageService.getSortedMessagesByUserId(user.getId()).forEach(messageServiceModel -> {
                if(messageServiceModel.isUnread()){
                    modelAndView.addObject("unreadMessages", true);
                }
            });
        }

        if (customer != null) {
            modelAndView.addObject("customer", customer);
            TrainingPlanServiceModel trainingPlan = this.trainingPlanService.findByCustomer(customer);
            CustomerTrainingPlanInfoServiceModel customerTrainingPlanInfoServiceModel = this.customerTrainingPlanInfoService.findByCustomer(customer);

            if (customerTrainingPlanInfoServiceModel != null) {
                int days = Period.between(customerTrainingPlanInfoServiceModel.getStartedOn(), LocalDate.now()).getDays();
                modelAndView.addObject("trainingPlan", trainingPlan);
                modelAndView.addObject("paid",customerTrainingPlanInfoServiceModel.isPaid());
                if (days <= TRAINING_PLAN_DURATION_DAYS && days >= 0) {
                    modelAndView.addObject("daysLeft", TRAINING_PLAN_DURATION_DAYS - days);
                } else if (days < 0) {
                    modelAndView.addObject("daysUntil", Period.between(LocalDate.now(), customerTrainingPlanInfoServiceModel.getStartedOn()).getDays());
                }
            }
            modelAndView.addObject("progressChart", customer.getProgressChart());
        } else {
            modelAndView.addObject("progressChart", new ProgressChart());
        }
        return modelAndView;
    }

}
