package project.gladiators.web;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.web.controllers.BaseController;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    @ExceptionHandler({Throwable.class})
    public ModelAndView handleExceptions(Throwable e) {
         ModelAndView modelAndView = new ModelAndView("error");
        Throwable throwable = e;
        if (throwable instanceof AccessDeniedException){
            return super.redirect("/home");
        }
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }

        modelAndView.addObject("message", throwable.getMessage());

        return modelAndView;
    }
}
