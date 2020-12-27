package project.gladiators.validators.trainer;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.TrainerRegisterBindingModel;

import java.time.LocalDate;

import static project.gladiators.constants.validators.TrainerConstants.*;

@Validator
public class TrainerRegisterValidator implements org.springframework.validation.Validator {


    @Override
    public boolean supports(Class<?> aClass) {
        return TrainerRegisterBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        TrainerRegisterBindingModel trainerRegisterBindingModel = (TrainerRegisterBindingModel) o;

        if(trainerRegisterBindingModel.getFirstName().isEmpty()){
            errors.rejectValue("firstName", FIRST_NAME_IS_EMPTY,
                    FIRST_NAME_IS_EMPTY);
        }
        if(trainerRegisterBindingModel.getLastName().isEmpty()){
            errors.rejectValue("lastName", LAST_NAME_IS_EMPTY,
                    LAST_NAME_IS_EMPTY);
        }
        if(trainerRegisterBindingModel.getGender() == null){
            errors.rejectValue("gender", GENDER_IS_EMPTY,
                    GENDER_IS_EMPTY);
        }
        if(trainerRegisterBindingModel.getDateOfBirth() == null){
            errors.rejectValue("dateOfBirth", DATE_IS_NULL,
                    DATE_IS_NULL);
        }else {
            if(trainerRegisterBindingModel.getDateOfBirth().isAfter(LocalDate.now())){
                errors.rejectValue("dateOfBirth", BIRTH_DATE_IS_IN_THE_FUTURE,
                        BIRTH_DATE_IS_IN_THE_FUTURE);
            }
        }


        if (trainerRegisterBindingModel.getYearsOfExperience() < 0){
            errors.rejectValue("yearsOfExperience", YEARS_OF_EXPERIENCE_MUST_BE_POSITIVE,
                    YEARS_OF_EXPERIENCE_MUST_BE_POSITIVE);
        }
        if(trainerRegisterBindingModel.getDescription().length() < 50 || trainerRegisterBindingModel
        .getDescription().length() > 1000){
            errors.rejectValue("description", DESCRIPTION_LENGTH_IS_INCORRECT,
                    DESCRIPTION_LENGTH_IS_INCORRECT);
        }
    }
}
