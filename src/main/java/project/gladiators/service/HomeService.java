package project.gladiators.service;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

public interface HomeService {

    ModelAndView mvcService(String name, ModelAndView modelAndView, HttpSession session);
}
