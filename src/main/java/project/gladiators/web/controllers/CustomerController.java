package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.model.bindingModels.CustomerRegisterBindingModel;
import project.gladiators.model.entities.User;
import project.gladiators.service.CustomerService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.CustomerServiceModel;

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
    @PreAuthorize("isAuthenticated()")
    public ModelAndView registration() {
        return super.view("customer-registration");
    }

    @PostMapping("/registration")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView confirmRegistration(@ModelAttribute CustomerRegisterBindingModel customer,Principal principal) throws IOException {
        //TODO : Метода за снимката
//        this.userService.setImageUrl(principal.getName(),customer.getImageUrl());
        this.customerService.registerCustomer(this.modelMapper.map(customer,CustomerServiceModel.class));
        return super.redirect("/home");
    }
}
