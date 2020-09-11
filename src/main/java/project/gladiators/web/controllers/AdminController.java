package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.annotations.PageTitle;
import project.gladiators.exceptions.InvalidChangeTrainerStatusException;
import project.gladiators.model.bindingModels.ArticleRegisterBindingModel;
import project.gladiators.model.bindingModels.RoleChangeBindingModel;
import project.gladiators.model.enums.Action;
import project.gladiators.service.*;
import project.gladiators.service.serviceModels.ArticleServiceModel;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    private UserService userService;
    private RoleService roleService;
    private CloudinaryService cloudinaryService;
    private ModelMapper modelMapper;
    private ArticleService articleService;
    private TrainerService trainerService;


    @Autowired
    public AdminController(UserService userService, RoleService roleService, CloudinaryService cloudinaryService, ModelMapper modelMapper, ArticleService articleService, TrainerService trainerService) {
        this.userService = userService;
        this.roleService = roleService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
        this.articleService = articleService;
        this.trainerService = trainerService;
    }


    @GetMapping("/user-management")
    @PageTitle("User management")
    public ModelAndView userRoles() {


        return super.view("/admin/user-management", new ModelAndView().
                addObject("users", this.userService.getAllUsers()));
    }


    @PostMapping("/user-management/change")
    public ModelAndView userRoles(@RequestParam("id") String id,
                                  ModelAndView modelAndView,
                                  RoleChangeBindingModel roleChangeBindingModel) {

        UserServiceModel user = this.userService
                .findById(id);

        RoleServiceModel role = new RoleServiceModel();
        role.setAuthority(roleChangeBindingModel.getRole());


        if (role.getAuthority() != null) {
            this.userService.addRoleToUser(user, role);
        }

        modelAndView.addObject("users", this.userService
                .getAllUsers());
        return view("/admin/user-management", modelAndView);
    }


    @GetMapping("/user-management/ban")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
    public ModelAndView banUser(@RequestParam("id") String id,
                                ModelAndView modelAndView) {

        this.userService
                .banUser(id);

        modelAndView.addObject("users", this.userService.getAllUsers());
        return view("/admin/all-users", modelAndView);
    }


    @GetMapping("/article-add")
    @PageTitle("Add article")
    public ModelAndView addArticle(ModelAndView modelAndView) {

        modelAndView.addObject("article", new ArticleRegisterBindingModel());

        return super.view("/admin/article-add",
                modelAndView);
    }

    @PostMapping("/article-add")
    public ModelAndView addArticle(@Valid @ModelAttribute("article") ArticleRegisterBindingModel articleRegisterBindingModel, BindingResult result, Principal principal
            , RedirectAttributes redirectAttributes) throws IOException {

        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("article", articleRegisterBindingModel);
            modelAndView.addObject("org.springframework.validation.BindingResult.article", result);
            return super.view("/admin/article-add", modelAndView);
        }
        String imageUrl = articleRegisterBindingModel.getImage().isEmpty() ? "https://res.cloudinary.com/gladiators/image/upload/v1599061356/No-image-found_vtfx1x.jpg"
                : this.cloudinaryService.uploadImageToCurrentFolder(articleRegisterBindingModel.getImage(), "articles");
        ArticleServiceModel articleServiceModel = modelMapper.map(articleRegisterBindingModel, ArticleServiceModel.class);
        articleServiceModel.setImageUrl(imageUrl);

        articleService.registerArticle(articleServiceModel, principal.getName());
        redirectAttributes.addFlashAttribute("statusMessage", "You created article successful");
        redirectAttributes.addFlashAttribute("statusCode", "successful");
        return super.redirect("/admin/article-add");
    }

    @GetMapping("/trainer-manager")
    @PageTitle("Trainer manager")
    public ModelAndView addTrainer(ModelAndView modelAndView) {
        List<String> users = userService.getAllUsers().stream().map(e -> e.getUsername()).collect(Collectors.toList());
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
}
