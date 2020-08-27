package project.gladiators.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController{

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView login() {
        return super.view("home");
    }
}
