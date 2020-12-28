package project.gladiators.validators.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.UserEditBindingModel;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.UserServiceModel;

import java.util.regex.Pattern;

import static project.gladiators.constants.validators.UserConstants.*;
import static project.gladiators.constants.validators.UserConstants.PASSWORDS_DO_NOT_MATCH;

@Validator
public class UserChangePasswordValidator implements org.springframework.validation.Validator {

    private final BCryptPasswordEncoder encoder;
    private final UserService userService;

    public UserChangePasswordValidator(BCryptPasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

       UserEditBindingModel userEditBindingModel = (UserEditBindingModel) o;
        UserServiceModel userServiceModel = this.userService.findUserByUsername(userEditBindingModel.getUsername());

        if(userEditBindingModel.getOldPassword().isEmpty()){
            errors.rejectValue("oldPassword", PASSWORD_CANNOT_BE_EMPTY,
                    PASSWORD_CANNOT_BE_EMPTY);
        }else {
            if(!encoder.matches(userEditBindingModel.getOldPassword(), userServiceModel.getPassword())){
                errors.rejectValue("oldPassword", OLD_PASSWORD_ERROR,
                        OLD_PASSWORD_ERROR);

            }
            Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{6,}\\S+$");
            if(!pattern.matcher(userEditBindingModel.getPassword()).matches()){
                errors.rejectValue("password", PASSWORD_NOT_VALID,
                        PASSWORD_NOT_VALID);
            }
        }
        if(errors.hasErrors()){
            return;
        }
        if(userEditBindingModel.getPassword() == null || userEditBindingModel.getConfirmPassword() == null){
            errors.rejectValue("password", PASSWORD_CANNOT_BE_EMPTY,
                    PASSWORD_CANNOT_BE_EMPTY);
        }else {
            if (!userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword())) {
                errors.rejectValue("password", PASSWORDS_DO_NOT_MATCH,
                        PASSWORDS_DO_NOT_MATCH);
            }
        }
    }
}
