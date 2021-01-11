package project.gladiators.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.model.entities.ProgressChart;
import project.gladiators.service.CustomerService;
import project.gladiators.service.CustomerTrainingPlanInfoService;
import project.gladiators.service.TrainingPlanService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.CustomerTrainingPlanInfoServiceModel;
import project.gladiators.service.serviceModels.TrainingPlanServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Controller
public class HomeController extends BaseController{

    private final UserService userService;
    private final CustomerService customerService;
    private final TrainingPlanService trainingPlanService;
    private final CustomerTrainingPlanInfoService customerTrainingPlanInfoService;
    private final static int TRAINING_PLAN_DURATION_DAYS = 28;


    public HomeController(UserService userService, CustomerService customerService, TrainingPlanService trainingPlanService, CustomerTrainingPlanInfoService customerTrainingPlanInfoService) {
        this.userService = userService;
        this.customerService = customerService;
        this.trainingPlanService = trainingPlanService;
        this.customerTrainingPlanInfoService = customerTrainingPlanInfoService;
    }

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Welcome")
    public ModelAndView index( ModelAndView modelAndView) {
        return view("index", modelAndView);

    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Home")
    public ModelAndView getHome(Principal principal, ModelAndView modelAndView) {
        UserServiceModel user =
                this.userService.findUserByUsername(principal.getName());
            CustomerServiceModel customer =
                    this.customerService.findCustomerByUser(user);
            if(customer != null){
            modelAndView.addObject("customer", customer);
            TrainingPlanServiceModel trainingPlan = this.trainingPlanService.findByCustomer(customer);
            CustomerTrainingPlanInfoServiceModel customerTrainingPlanInfoServiceModel =
                    this.customerTrainingPlanInfoService.findByCustomer(customer);
            if(customerTrainingPlanInfoServiceModel != null) {
                int days = Period.between(customerTrainingPlanInfoServiceModel.getStartedOn(), LocalDate.now()).getDays();
                    modelAndView.addObject("trainingPlan",
                            trainingPlan);
                if (days <= TRAINING_PLAN_DURATION_DAYS && days >= 0) {
                    modelAndView.addObject("daysLeft", TRAINING_PLAN_DURATION_DAYS - days);
                }else if(days < 0) {
                    modelAndView.addObject("daysUntil", Period.between(LocalDate.now(), customerTrainingPlanInfoServiceModel.getStartedOn()).getDays());
                }
            }
                modelAndView.addObject("progressChart", customer.getProgressChart());
            }else{
                modelAndView.addObject("progressChart", new ProgressChart());
            }

        return super.view("home", modelAndView);
    }



}
