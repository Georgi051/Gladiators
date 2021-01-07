package project.gladiators.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.model.entities.ProgressChart;
import project.gladiators.service.CustomerService;
import project.gladiators.service.TrainingPlanService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.security.Principal;

@Controller
public class HomeController extends BaseController{

    private final UserService userService;
    private final CustomerService customerService;
    private final TrainingPlanService trainingPlanService;


    public HomeController(UserService userService, CustomerService customerService, TrainingPlanService trainingPlanService) {
        this.userService = userService;
        this.customerService = customerService;
        this.trainingPlanService = trainingPlanService;
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
        if(customerService.findCustomerByUser(user) != null){
            CustomerServiceModel customer =
                    this.customerService.findCustomerByUser(user);
            modelAndView.addObject("customer", customer);
            modelAndView.addObject("trainingPlan",
                    this.trainingPlanService.findByCustomer(customer));
            modelAndView.addObject("progressChart", customer.getProgressChart());
        }else{
            modelAndView.addObject("progressChart", new ProgressChart());
        }

        return super.view("home", modelAndView);
    }



}
