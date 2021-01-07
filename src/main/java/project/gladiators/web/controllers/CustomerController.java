package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.model.bindingModels.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.gladiators.model.bindingModels.CustomerRegisterBindingModel;
import project.gladiators.service.*;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.TrainerServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;
import project.gladiators.validators.customer.CustomerRegistrationValidator;
import project.gladiators.validators.customer.ProgressChartValidator;
import project.gladiators.validators.customer.SendMessageValidator;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;


@Controller
@RequestMapping("/customers")
public class CustomerController extends BaseController{
    private final CustomerService customerService;
    private final UserService userService;
    private final TrainerService trainerService;
    private final MessageService messageService;
    private final TrainingPlanService trainingPlanService;
    private final ModelMapper modelMapper;
    private final CustomerRegistrationValidator customerRegistrationValidator;
    private final ProgressChartValidator progressChartValidator;
    private final SendMessageValidator sendMessageValidator;

    @Autowired
    public CustomerController(CustomerService customerService, UserService userService, TrainerService trainerService, MessageService messageService, TrainingPlanService trainingPlanService, ModelMapper modelMapper, CustomerRegistrationValidator customerRegistrationValidator, ProgressChartValidator progressChartValidator, SendMessageValidator sendMessageValidator) {
        this.customerService = customerService;
        this.userService = userService;
        this.trainerService = trainerService;
        this.messageService = messageService;
        this.trainingPlanService = trainingPlanService;
        this.modelMapper = modelMapper;
        this.customerRegistrationValidator = customerRegistrationValidator;
        this.progressChartValidator = progressChartValidator;
        this.sendMessageValidator = sendMessageValidator;
    }


    @GetMapping("/registration")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PageTitle("Customer Registration")
    public ModelAndView registration(@ModelAttribute(name = "customer") CustomerRegisterBindingModel customer, ModelAndView modelAndView) {
        modelAndView.addObject("customer", customer);
        return super.view("customer/customer-registration",modelAndView);
    }

    @PostMapping("/registration")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PageTitle("Customer Registration")
    public ModelAndView confirmRegistration(@Valid @ModelAttribute(name = "customer")
                                                        CustomerRegisterBindingModel customer
            , BindingResult bindingResult, ModelAndView modelAndView) throws IOException {

        customerRegistrationValidator.validate(customer,bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("customer", customer);
            return super.view("customer/customer-registration", modelAndView);
        }

        this.customerService.registerCustomer(this.modelMapper.map(customer,CustomerServiceModel.class),customer.getImageUrl());
        return super.redirect("/home");
    }


    @GetMapping("/progressChart/edit")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PageTitle("Edit Progress Chart")
    public ModelAndView getEditProgressChart(ModelAndView modelAndView,
                                  ProgressChartEditBindingModel progressChartEditBindingModel, Principal principal){
        UserServiceModel userServiceModel = this.userService.findUserByUsername(principal.getName());
        CustomerServiceModel customerServiceModel = this.customerService.findCustomerByUser(userServiceModel);
        modelAndView.addObject("progressChartEditBindingModel", customerServiceModel.getProgressChart());
        return super.view("user/edit-progressChart", modelAndView);
    }

    @PostMapping("/progressChart/edit")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PageTitle("Home - Gladiators")
    public ModelAndView editProgressChart(@Valid @ModelAttribute("progressChartEditBindingModel")
                                                      ProgressChartEditBindingModel progressChartEditBindingModel,
                                          BindingResult bindingResult,
                                          ModelAndView modelAndView,
                                          Principal principal){
        progressChartValidator.validate(progressChartEditBindingModel,bindingResult);
        if(bindingResult.hasErrors()){
            modelAndView.addObject("progressChartEditBindingModel", progressChartEditBindingModel);
            modelAndView.addObject("org.springframework.validation.BindingResult.progressChartEditBindingModel", bindingResult);
            return super.view("/user/edit-progressChart", modelAndView);
        }

        UserServiceModel userServiceModel = this.userService.findUserByUsername(principal.getName());
        CustomerServiceModel customer = this.customerService.findCustomerByUser(userServiceModel);

        customerService.editProgressChart(customer, progressChartEditBindingModel);
        modelAndView.addObject("trainingPlan",
                this.trainingPlanService.findByCustomer(customer));
        modelAndView.addObject("progressChart", customer.getProgressChart());
        return super.view("home", modelAndView);

    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PageTitle("Send message")
    @GetMapping("/sendMessageToTrainer")
    public ModelAndView getSendMessage(@RequestParam("id") String id,
                                       ModelAndView modelAndView){

        TrainerServiceModel trainer = this.trainerService.findById(id);
        modelAndView.addObject("trainer", trainer);
        SendMessageBindingModel sendMessageBindingModel = new SendMessageBindingModel();
        sendMessageBindingModel.setMessageTo(trainer.getId());
        modelAndView.addObject("sendMessageBindingModel", sendMessageBindingModel);
        return super.view("/trainer/contact-with-trainer", modelAndView);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PageTitle("Send Message")
    @PostMapping("/sendMessageToTrainer")
    public ModelAndView sendMessage(@RequestParam("id") String id,
                                    @Valid @ModelAttribute("sendMessageBindingModel")
                                                SendMessageBindingModel sendMessageBindingModel,
                                    BindingResult bindingResult, ModelAndView modelAndView,
                                    Principal principal){

        sendMessageValidator.validate(sendMessageBindingModel,bindingResult);
        if(bindingResult.hasErrors()){
            sendMessageBindingModel.setMessageTo(this.trainerService.findById(id).getId());
            TrainerServiceModel trainer = this.trainerService.findById(id);
            modelAndView.addObject("sendMessageBindingModel", sendMessageBindingModel);
            modelAndView.addObject("trainer", trainer);
            return super.view("trainer/contact-with-trainer", modelAndView);
        }

        UserServiceModel userServiceModel = this.userService
                .findUserByUsername(principal.getName());
        this.messageService.sendMessage(userServiceModel, this.trainerService.findById(id)
                .getUser().getId(), sendMessageBindingModel.getMessage(), sendMessageBindingModel.getTitle());
        return redirect("/trainers");

    }

}
