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
import project.gladiators.service.CustomerService;
import project.gladiators.service.MessageService;
import project.gladiators.service.TrainerService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.TrainerServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

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
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerController(CustomerService customerService, UserService userService, TrainerService trainerService, MessageService messageService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.userService = userService;
        this.trainerService = trainerService;
        this.messageService = messageService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/registration")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ModelAndView registration(@ModelAttribute(name = "customer") CustomerRegisterBindingModel customer, ModelAndView modelAndView) {
        modelAndView.addObject("customer", customer);
        return super.view("customer/customer-registration",modelAndView);
    }

    @PostMapping("/registration")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ModelAndView confirmRegistration(@Valid @ModelAttribute(name = "customer")
                                                        CustomerRegisterBindingModel customer
            , BindingResult bindingResult, ModelAndView modelAndView) throws IOException {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("customer", customer);
            return super.view("customer/customer-registration", modelAndView);
        }

        this.customerService.registerCustomer(this.modelMapper.map(customer,CustomerServiceModel.class),customer.getImageUrl());
        return super.redirect("/home");
    }


    @GetMapping("/progressChart/edit")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ModelAndView getEditProgressChart(ModelAndView modelAndView,
                                  ProgressChartEditBindingModel progressChartEditBindingModel){

        modelAndView.addObject("progressChartEditBindingModel", progressChartEditBindingModel);
        return super.view("user/edit-progressChart", modelAndView);
    }

    @PostMapping("/progressChart/edit")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ModelAndView editProgressChart(@Valid @ModelAttribute("progressChartEditBindingModel")
                                                      ProgressChartEditBindingModel progressChartEditBindingModel,
                                          BindingResult bindingResult,
                                          ModelAndView modelAndView,
                                          Principal principal){

        if(bindingResult.hasErrors()){
            modelAndView.addObject("progressChartEditBindingModel", progressChartEditBindingModel);
            modelAndView.addObject("org.springframework.validation.BindingResult.progressChartEditBindingModel", bindingResult);
            return super.view("/user/edit-progressChart", modelAndView);
        }

        UserServiceModel userServiceModel = this.userService.findUserByUsername(principal.getName());
        CustomerServiceModel customer = this.customerService.findCustomerByUser(userServiceModel);

        customerService.editProgressChart(customer, progressChartEditBindingModel);

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
    @PostMapping("/sendMessageToTrainer")
    public ModelAndView sendMessage(@RequestParam("id") String id,
                                    @Valid @ModelAttribute("sendMessageBindingModel")
                                                SendMessageBindingModel sendMessageBindingModel,
                                    BindingResult bindingResult,
                                    ModelAndView modelAndView,
                                    Principal principal){

        if(bindingResult.hasErrors()){
            sendMessageBindingModel.setMessageTo(id);
            modelAndView.addObject("sendMessageBindingModel", sendMessageBindingModel);
            TrainerServiceModel trainer = this.trainerService.findById(sendMessageBindingModel.getMessageTo());
            modelAndView.addObject("trainer", trainer);
            return super.view("trainer/contact-with-trainer", modelAndView);
        }

        UserServiceModel userServiceModel = this.userService
                .findUserByUsername(principal.getName());
        this.messageService.sendMessage(userServiceModel, id, sendMessageBindingModel.getMessage(), sendMessageBindingModel.getTitle());
        return redirect("/trainers");

    }


}
