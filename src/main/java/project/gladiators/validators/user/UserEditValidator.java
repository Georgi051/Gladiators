package project.gladiators.validators.user;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.UserEditBindingModel;

import java.util.regex.Pattern;

import static project.gladiators.validators.user.UserConstants.*;
import static project.gladiators.validators.user.UserConstants.PASSWORD_NOT_VALID;

@Validator
public class UserEditValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserEditBindingModel userEditBindingModel = (UserEditBindingModel) o;


        if(userEditBindingModel.getFirstName().length() == 0){
            errors.rejectValue("firstName", FIRST_NAME_LENGTH_IS_INVALID,
                    FIRST_NAME_LENGTH_IS_INVALID);
        }
        if(userEditBindingModel.getLastName().length() == 0){
            errors.rejectValue("lastName", LAST_NAME_LENGTH_IS_INVALID,
                    LAST_NAME_LENGTH_IS_INVALID);
        }

    }
}
