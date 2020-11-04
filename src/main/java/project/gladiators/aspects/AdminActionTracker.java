package project.gladiators.aspects;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import project.gladiators.model.enums.Action;
import project.gladiators.service.LoggerService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@Aspect
public class AdminActionTracker {
    private final UserService userService;
    private final LoggerService loggerService;

    @Autowired
    public AdminActionTracker(UserService userService, LoggerService loggerService) {
        this.userService = userService;
        this.loggerService = loggerService;
    }

    @Pointcut("execution(* project.gladiators.web.controllers.AdminController.Post*(..))")
    public void trackAdmin() {
    }


    @After("trackAdmin()")
    public void log (JoinPoint joinPoint) {
        UserServiceModel user= this.userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        String description= joinPoint.getSignature().getName().substring(4);
        Action action;

        if(description.toLowerCase().contains("delete")){
            action=Action.DELETE;
        }else if(description.toLowerCase().contains("change")){
            action=Action.CHANGE;
        }else if (description.toLowerCase().contains("create")){
            action=Action.CREATE;
        }else{
            action=Action.BAN;
        }

        this.loggerService.log(action,description, LocalDateTime.now(ZoneId.systemDefault()),user);
    }
}
