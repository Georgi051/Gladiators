package project.gladiators.service;

import org.springframework.web.servlet.ModelAndView;

public interface HomeService {

    ModelAndView mvcService(String name, ModelAndView modelAndView);
}
