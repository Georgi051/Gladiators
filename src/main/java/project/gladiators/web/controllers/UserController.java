package project.gladiators.web.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.annotations.PageTitle;
import project.gladiators.events.OnRegistrationCompleteEvent;
import project.gladiators.exceptions.UserNotFoundException;
import project.gladiators.model.bindingModels.UserEditBindingModel;
import project.gladiators.model.bindingModels.UserRegisterBindingModel;
import project.gladiators.model.entities.User;
import project.gladiators.model.entities.VerificationToken;
import project.gladiators.service.MessageService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.UserServiceModel;
import project.gladiators.validators.user.UserChangePasswordValidator;
import project.gladiators.validators.user.UserEditValidator;
import project.gladiators.validators.user.UserRegisterValidator;
import project.gladiators.web.viewModels.UserViewModel;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final MessageService messageService;
    private final UserRegisterValidator userRegisterValidator;
    private final UserEditValidator userEditValidator;
    private final UserChangePasswordValidator userChangePasswordValidator;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageSource messages;

    public UserController(ModelMapper modelMapper, UserService userService,
                          MessageService messageService, UserRegisterValidator userRegisterValidator,
                          UserEditValidator userEditValidator, UserChangePasswordValidator userChangePasswordValidator,
                          ApplicationEventPublisher eventPublisher, MessageSource messages) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.messageService = messageService;
        this.userRegisterValidator = userRegisterValidator;
        this.userEditValidator = userEditValidator;
        this.userChangePasswordValidator = userChangePasswordValidator;
        this.eventPublisher = eventPublisher;
        this.messages = messages;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Register")
    public ModelAndView register(@ModelAttribute(name = "model") UserRegisterBindingModel model, ModelAndView modelAndView) {
        modelAndView.addObject("model", model);
        return super.view("register", modelAndView);
    }

    @PostMapping("/register")
    @PageTitle("Register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@Valid @ModelAttribute(name = "model") UserRegisterBindingModel model
            , HttpServletRequest request, BindingResult bindingResult, ModelAndView modelAndView,
                                        RedirectAttributes redirectAttributes) {
        userRegisterValidator.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", model);
            return super.view("register", modelAndView);
        }

        UserServiceModel userServiceModel =
                this.userService.registerUser(this.modelMapper.map(model, UserServiceModel.class));

        String appUrl = request.getContextPath();
        User user = this.modelMapper
                .map(this.userService.findById(userServiceModel.getId()), User.class);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user,
                request.getLocale(), appUrl));

        redirectAttributes.addFlashAttribute("statusMessage", "Please check your email to verify your account");
        redirectAttributes.addFlashAttribute("statusCode", "successful");
        return super.redirect("/users/register");
    }


    @GetMapping("/registrationConfirm")
    @PageTitle("Register")
    public ModelAndView confirmRegistration
            (WebRequest request, ModelAndView model, @RequestParam("token") String token,
             RedirectAttributes redirectAttributes) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addObject("message", message);
            return super.view("register", model);
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addObject("message", "Your verification has expired!");

            return super.view("login", model);
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        redirectAttributes.addFlashAttribute("statusMessage", "You have verified your account! You can log now!");
        redirectAttributes.addFlashAttribute("statusCode", "successful");
        return super.redirect("/users/login");
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Login")
    public ModelAndView login() {
        return super.view("login");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("User management")
    public ModelAndView getAllUsers(Model model) {

        model.addAttribute("users", this.userService.getAllUsers());
        return super.view("admin/all-users");
    }

    @GetMapping("/")
    @PreAuthorize("!hasRole('ROLE_ROOT')")
    @PageTitle("User Profile")
    public ModelAndView userProfile(@RequestParam("id") String id) {

        return super.view("user/profile-page",
                new ModelAndView().addObject("user", this.modelMapper
                        .map(this.userService.findById(id), UserViewModel.class)));
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit User")
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
    @PageTitle("Edit User")
    public ModelAndView editProfile(@RequestParam("id") String id,
                                    @Valid @ModelAttribute(name = "userEditBindingModel")
                                            UserEditBindingModel userEditBindingModel,
                                    BindingResult bindingResult,
                                    ModelAndView modelAndView) {
        userEditValidator.validate(userEditBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
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
    @PageTitle("Edit User")
    public ModelAndView getChangePassword(@RequestParam("id") String id,
                                          ModelAndView modelAndView,
                                          @ModelAttribute(name = "userEditBindingModel")
                                                  UserEditBindingModel userEditBindingModel) throws UserNotFoundException {

        UserViewModel userViewModel = this.modelMapper
                .map(this.userService.findById(id), UserViewModel.class);
        userEditBindingModel = modelMapper.map(userViewModel, UserEditBindingModel.class);
        modelAndView.addObject("userEditBindingModel", userEditBindingModel);
        modelAndView.setViewName("user/change-password");
        return modelAndView;
    }


    @PostMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit User")
    public ModelAndView changePassword(@RequestParam("id") String id,
                                       @Valid @ModelAttribute(name = "userEditBindingModel")
                                               UserEditBindingModel userEditBindingModel,
                                       BindingResult bindingResult,
                                       ModelAndView modelAndView) {

        userChangePasswordValidator.validate(userEditBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("userEditBindingModel", userEditBindingModel);
            return view("user/change-password", modelAndView);
        }
        this.userService.changeUserPassword(userEditBindingModel);
        return super.redirect(String.format("/users/?id=%s", id));
    }

    @GetMapping("/changePicture")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit User")
    public ModelAndView changePicture(@ModelAttribute(name = "userEditBindingModel")
                                              UserEditBindingModel userEditBindingModel,
                                      ModelAndView modelAndView
    ) throws UserNotFoundException {
        modelAndView.addObject("userEditBindingModel", userEditBindingModel);
        return super.view("user/change-profilePicture", modelAndView);
    }


    @PostMapping("/changePicture")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit User")
    public ModelAndView changeProfilePicture(@RequestParam("id") String id,
                                             @Valid @ModelAttribute(name = "userEditBindingModel")
                                                     UserEditBindingModel userEditBindingModel,
                                             BindingResult bindingResult,
                                             ModelAndView modelAndView) throws IOException {

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("userEditBindingModel", userEditBindingModel);
            return view("user/change-profilePicture", modelAndView);
        }
        UserServiceModel userServiceModel = this.userService.findById(id);
        this.userService.changeProfilePicture(userServiceModel, userEditBindingModel.getImageUrl());
        modelAndView.addObject("user", userServiceModel);
        return super.redirect(String.format("/users/?id=%s", id));
    }

    @GetMapping("/inbox/")
    @PageTitle("Your Messages")
    public ModelAndView inbox(@RequestParam("id") String id, ModelAndView modelAndView) {

        modelAndView.addObject("messages", this.messageService.getSortedMessagesByUserId(id));
        return super.view("user/inbox", modelAndView);
    }

}
