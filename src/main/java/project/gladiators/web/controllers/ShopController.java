package project.gladiators.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;

@Controller
public class ShopController extends BaseController {


    @GetMapping("/shop")
    @PageTitle("Welcome")
    public ModelAndView index(ModelAndView modelAndView) {
        return view("shop", modelAndView);
    }
}
