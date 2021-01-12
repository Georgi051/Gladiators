package project.gladiators.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.service.HomeService;

import java.security.Principal;

@Controller
public class HomeController extends BaseController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;

    }

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Welcome")
    public ModelAndView index(ModelAndView modelAndView) {
        return view("index", modelAndView);

    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Home")
    public ModelAndView getHome(Principal principal, ModelAndView modelAndView) {
        ModelAndView view = homeService.mvcService(principal.getName(), modelAndView);
        return super.view("home", view);
    }

}
