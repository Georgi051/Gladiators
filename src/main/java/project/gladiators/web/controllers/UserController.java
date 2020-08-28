package project.gladiators.web.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.model.bindingModels.UserRegisterBindingModel;

import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.UserServiceModel;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
    private final ModelMapper modelMapper;
    private final UserService userService;

    public UserController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register() {
        return super.view("register");
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@ModelAttribute UserRegisterBindingModel model) {
        UserServiceModel userServiceModel =
                this.userService.registerUser(this.modelMapper.map(model, UserServiceModel.class), model);
        if (userServiceModel == null) {
            return super.view("register");
        }
        return super.redirect("/login");
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login() {
        return super.view("login");
    }

}
