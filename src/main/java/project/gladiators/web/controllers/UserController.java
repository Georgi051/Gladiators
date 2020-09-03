package project.gladiators.web.controllers;


import com.google.gson.Gson;
import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.model.dtos.MuscleDto;
import project.gladiators.model.bindingModels.RoleChangeBindingModel;
import project.gladiators.model.bindingModels.UserRegisterBindingModel;

import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static project.gladiators.constants.GlobalConstants.MUSCLES_FILE_PATH;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final Gson gson;

    public UserController(ModelMapper modelMapper, UserService userService, Gson gson) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.gson = gson;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(@ModelAttribute(name = "model") UserRegisterBindingModel model, ModelAndView modelAndView) {
        modelAndView.addObject("model", model);
        return super.view("register",modelAndView);
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@Valid @ModelAttribute(name = "model") UserRegisterBindingModel model
            , BindingResult bindingResult,ModelAndView modelAndView) throws FileNotFoundException {
        MuscleDto[] muscles =
                this.gson.fromJson(new FileReader(MUSCLES_FILE_PATH), MuscleDto[].class);

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            return super.view("register", modelAndView);
        }

        UserServiceModel userServiceModel =
                this.userService.registerUser(this.modelMapper.map(model, UserServiceModel.class), model,muscles);
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

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession httpSession){
        httpSession.invalidate();
        return view("index");
    }



}
