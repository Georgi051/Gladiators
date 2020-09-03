package project.gladiators.web.controllers;


import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.model.dtos.MuscleDto;
import project.gladiators.model.bindingModels.RoleChangeBindingModel;
import project.gladiators.model.bindingModels.UserRegisterBindingModel;

import project.gladiators.model.entities.User;
import project.gladiators.model.enums.Gender;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;
import project.gladiators.web.viewModels.ArticleViewModel;
import project.gladiators.web.viewModels.UserViewModel;

import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.FileReader;

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
    public ModelAndView register() {
        return super.view("register");
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@ModelAttribute UserRegisterBindingModel model) throws FileNotFoundException {
        MuscleDto[] muscles =
                this.gson.fromJson(new FileReader(MUSCLES_FILE_PATH), MuscleDto[].class);
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
        return super.view("user/profile-page",
                new ModelAndView().addObject("user", user));
    }
}
