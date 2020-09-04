package project.gladiators.web.controllers;


import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.exceptions.UserNotFoundException;
import project.gladiators.model.bindingModels.RoleChangeBindingModel;
import project.gladiators.model.bindingModels.UserEditBindingModel;
import project.gladiators.model.bindingModels.UserRegisterBindingModel;
import project.gladiators.model.dtos.MuscleDto;
import project.gladiators.model.entities.User;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;
import project.gladiators.web.viewModels.UserViewModel;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.Period;

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

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllUsers(Model model){

        model.addAttribute("users", this.userService.getAllUsers());
        return "admin/all-users";
    }

    @PostMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView changeUserRole(@RequestParam("id") String id,
                                       ModelAndView modelAndView,
                                       RoleChangeBindingModel roleChangeBindingModel){

        UserServiceModel user = this.userService
                .findById(id);

        RoleServiceModel role = new RoleServiceModel();
        role.setAuthority(roleChangeBindingModel.getRole());

        if(role.getAuthority() != null){
            this.userService.addRoleToUser(user, role);
        }

        modelAndView.addObject("users", this.userService
                .getAllUsers());
        return view("admin/all-users", modelAndView);
    }

    @GetMapping("/ban")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView banUser(@RequestParam("id") String id,
                                ModelAndView modelAndView){

        this.userService
                .banUser(id);

        modelAndView.addObject("users", this.userService.getAllUsers());
        return view("admin/all-users", modelAndView);
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView userProfile(@RequestParam("id") String id){

        User userTest = this.modelMapper
        .map(this.userService.findById(id), User.class);

        UserViewModel user = this.modelMapper.map
                (userTest, UserViewModel.class);
        int age = Period.between(userTest.getDateOfBirth(), LocalDate.now()).getYears();
        user.setAge(age);
        return super.view("user/profile-page",
                new ModelAndView().addObject("user", user));
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editUser(@RequestParam("id") String id,
                                 ModelAndView modelAndView,
                                 @ModelAttribute(name = "userEditBindingModel")
                                         UserEditBindingModel userEditBindingModel) throws UserNotFoundException {
        UserServiceModel userServiceModel = this.userService.findById(id);
        userEditBindingModel = modelMapper.map(userServiceModel, UserEditBindingModel.class);
        modelAndView.addObject("user", userServiceModel);
        modelAndView.addObject("userEditBindingModel", userEditBindingModel);
        modelAndView.setViewName("user/edit-user");
        return modelAndView;
    }


    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfile(@RequestParam("id") String id,
                                    @Valid @ModelAttribute(name = "userEditBindingModel")
                                                UserEditBindingModel userEditBindingModel,
                                    BindingResult bindingResult,
                                    ModelAndView modelAndView){
        if(bindingResult.hasErrors()){
            modelAndView.addObject("userEditBindingModel", userEditBindingModel);
            return view("user/edit-user", modelAndView);
        }

        UserServiceModel userServiceModel = this.modelMapper.map(userEditBindingModel, UserServiceModel.class);

        this.userService.editUserProfile(userServiceModel);

        modelAndView.addObject("user", userServiceModel);
        return super.redirect(String.format("/users/?id=%s", id));
    }

    @GetMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getChangePassword(@RequestParam("id") String id,
                                 ModelAndView modelAndView,
                                 @ModelAttribute(name = "userEditBindingModel")
                                         UserEditBindingModel userEditBindingModel) throws UserNotFoundException {
        UserServiceModel userServiceModel = this.userService.findById(id);
        userEditBindingModel = modelMapper.map(userServiceModel, UserEditBindingModel.class);
        userEditBindingModel.setOldPassword(null);
        modelAndView.addObject("user", userServiceModel);
        modelAndView.addObject("userEditBindingModel", userEditBindingModel);
        modelAndView.setViewName("user/change-password");
        return modelAndView;
    }


    @PostMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView changePassword(@RequestParam("id") String id,
                                    @Valid @ModelAttribute(name = "userEditBindingModel")
                                            UserEditBindingModel userEditBindingModel,
                                    BindingResult bindingResult,
                                    ModelAndView modelAndView){
        if(bindingResult.hasErrors()){
            userEditBindingModel.setOldPassword(null);
            userEditBindingModel.setPassword(null);
            userEditBindingModel.setConfirmPassword(null);
            modelAndView.addObject("userEditBindingModel", userEditBindingModel);
            return view("user/edit-user", modelAndView);
        }

        UserServiceModel userServiceModel = this.modelMapper.map(userEditBindingModel, UserServiceModel.class);

        this.userService.changeUserPassword(userServiceModel, userEditBindingModel.getOldPassword());

        modelAndView.addObject("user", userServiceModel);
        return super.redirect(String.format("/users/?id=%s", id));
    }

}
