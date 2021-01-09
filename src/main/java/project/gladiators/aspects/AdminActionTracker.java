package project.gladiators.aspects;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import project.gladiators.model.bindingModels.RoleChangeBindingModel;
import project.gladiators.model.enums.Action;
import project.gladiators.service.LoggerService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static project.gladiators.constants.adminLog.AdminLogConstants.*;

@Aspect
@Configuration
public class AdminActionTracker {
    private final UserService userService;
    private final LoggerService loggerService;

    @Autowired
    public AdminActionTracker(UserService userService, LoggerService loggerService) {
        this.userService = userService;
        this.loggerService = loggerService;
    }
    
    @Pointcut("@annotation(project.gladiators.aspects.Admin)")
    public void trackAdmin() {
    }

    @After("trackAdmin()")
    public void log (JoinPoint joinPoint) {
        UserServiceModel user= this.userService.findUserByUsername(SecurityContextHolder.getContext()
                .getAuthentication().getName());
        String description= joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Action action = null;

        if(description.contains(USER_ROLES)){
            RoleChangeBindingModel role = (RoleChangeBindingModel) args[2];
            String changedRole = role.getRole();
            description = CHANGE_ROLE_MASSAGE + changedRole;
            action=Action.CHANGE;
        }else if (description.contains(ADD_TRAINER)){
            if (args[1].toString().equals(CREATE_ACTION)){
                description = GIVE_TRAINER_ROLE_MASSAGE;
                action=Action.CREATE;
            }else {
                description = REMOVE_MASSAGE;
                action=Action.DELETE;
            }
        }else if (description.contains(BAN_USER)){
            description = BAN_MASSAGE;
            action=Action.BAN;
        }
        this.loggerService.log(action,description, LocalDateTime.now(ZoneId.systemDefault()),user);
    }

}
