package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.model.bindingModels.CustomerRegisterBindingModel;
import project.gladiators.model.bindingModels.ProgressChartEditBindingModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.model.bindingModels.CustomerRegisterBindingModel;
import project.gladiators.model.bindingModels.UserRegisterBindingModel;
import project.gladiators.model.entities.User;
import project.gladiators.service.CustomerService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;


@Controller
@RequestMapping("/customers")
public class CustomerController extends BaseController{
    private final CustomerService customerService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerController(CustomerService customerService, UserService userService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.userService = userService;
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
    public ModelAndView confirmRegistration(@Valid @ModelAttribute(name = "customer") CustomerRegisterBindingModel customer
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
    public ModelAndView editProgressChart(ProgressChartEditBindingModel progressChartEditBindingModel,
                                          BindingResult bindingResult,
                                          ModelAndView modelAndView,
                                          Principal principal){

        if(bindingResult.hasErrors()){
            modelAndView.addObject("progressChartEditBindingModel", progressChartEditBindingModel);
            modelAndView.addObject("org.springframework.validation.BindingResult.progressChartEditBindingModel", bindingResult);
            return super.redirect("/customers/progressChart/edit");
        }

        UserServiceModel userServiceModel = this.userService.findUserByUsername(principal.getName());
        CustomerServiceModel customer = this.customerService.findCustomerByUser(userServiceModel);

        customerService.editProgressChart(customer, progressChartEditBindingModel);

        modelAndView.addObject("progressChart", customer.getProgressChart());
        return super.view("home", modelAndView);

    }
}
