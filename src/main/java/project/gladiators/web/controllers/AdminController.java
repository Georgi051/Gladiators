package project.gladiators.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.annotations.PageTitle;
import project.gladiators.exceptions.InvalidChangeTrainerStatusException;
import project.gladiators.model.bindingModels.RoleChangeBindingModel;
import project.gladiators.model.enums.Action;
import project.gladiators.service.TrainerService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    private final UserService userService;
    private final TrainerService trainerService;

    @Autowired
    public AdminController(UserService userService, TrainerService trainerService) {
        this.userService = userService;
        this.trainerService = trainerService;
    }


    @GetMapping("/user-management")
    @PageTitle("User management")
    public ModelAndView userRoles(Principal principal) {


        List<UserServiceModel> users = filteredUsers(principal);
        return super.view("/admin/user-management", new ModelAndView().
                addObject("users", users));
    }


    @PostMapping("/user-management/change")
    public ModelAndView userRoles(@RequestParam("id") String id,
                                  ModelAndView modelAndView,
                                  RoleChangeBindingModel roleChangeBindingModel,
                                  Principal principal) {

        UserServiceModel user = this.userService.findById(id);

        RoleServiceModel role = new RoleServiceModel();
        role.setAuthority(roleChangeBindingModel.getRole());

        List<UserServiceModel> users = filteredUsers(principal);
        if (role.getAuthority() != null) {
            this.userService.addRoleToUser(user, role);
        }

        modelAndView.addObject("users", users);
        return view("/admin/user-management", modelAndView);
    }

    @GetMapping("/user-management/ban")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
    public ModelAndView banUser(@RequestParam("id") String id,
                                ModelAndView modelAndView) {
        this.userService.banUser(id);
        modelAndView.addObject("users", this.userService.getAllUsers());
        return view("/admin/all-users", modelAndView);
    }

    @GetMapping("/trainer-manager")
    @PageTitle("Trainer manager")
    public ModelAndView addTrainer(ModelAndView modelAndView,Principal principal) {
        List<UserServiceModel> userServiceModels = filteredUsers(principal);
        List<String> users = userServiceModels.stream().map(UserServiceModel::getUsername).collect(Collectors.toList());
        modelAndView.addObject("users", users);
        return super.view("/admin/trainer-manager", modelAndView);
    }

    @PostMapping("/trainer-manager")
    public ModelAndView addTrainer(@RequestParam String username, @RequestParam("option") Action action,
                                   RedirectAttributes redirectAttributes
    ) {

        try {
            trainerService.changeTrainerStatus(username, action);
            String resultAction = Action.valueOf("CREATE").equals(action) ? "created" : "removed";

            redirectAttributes.addFlashAttribute("statusMessage", String.format("You %s trainer successful", resultAction));
            redirectAttributes.addFlashAttribute("statusCode", "successful");

        } catch (InvalidChangeTrainerStatusException ex) {
            redirectAttributes.addFlashAttribute("statusMessage", ex.getMessage());
            redirectAttributes.addFlashAttribute("statusCode", "error");

        }

        return super.redirect("/admin/trainer-manager");
    }

    private List<UserServiceModel> filteredUsers(Principal principal) {
        return this.userService.getAllUsers().stream()
                .filter(u -> !u.getUsername().equals(principal.getName()))
                .filter(u -> u.getAuthorities().stream().noneMatch(roleServiceModel -> roleServiceModel.getAuthority().equals("ROLE_ROOT")))
                .collect(Collectors.toList());
    }
}
