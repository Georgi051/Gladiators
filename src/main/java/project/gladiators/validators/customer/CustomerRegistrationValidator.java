package project.gladiators.validators.customer;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.CustomerRegisterBindingModel;

import java.time.LocalDate;

import static project.gladiators.constants.validators.CustomerConstants.*;

@Validator
public class CustomerRegistrationValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CustomerRegisterBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CustomerRegisterBindingModel customerRegisterBindingModel = (CustomerRegisterBindingModel) o;

        if (customerRegisterBindingModel.getFirstName().isEmpty()){
             errors.rejectValue("firstName",CUSTOMER_FIRST_NAME_CANNOT_BE_NULL,
                     CUSTOMER_FIRST_NAME_CANNOT_BE_NULL);
        }

        if (customerRegisterBindingModel.getLastName().isEmpty()){
            errors.rejectValue("lastName",CUSTOMER_LAST_NAME_CANNOT_BE_NULL,
                    CUSTOMER_LAST_NAME_CANNOT_BE_NULL);
        }

        if(customerRegisterBindingModel.getGender() == null){
            errors.rejectValue("gender", CUSTOMER_GENDER_CANNOT_BE_NULL,
                    CUSTOMER_GENDER_CANNOT_BE_NULL);
        }

        if(customerRegisterBindingModel.getDateOfBirth() == null){
            errors.rejectValue("dateOfBirth", CUSTOMER_DATE_IS_NULL,
                    CUSTOMER_DATE_IS_NULL);
        }else {
            if(customerRegisterBindingModel.getDateOfBirth().isAfter(LocalDate.now())){
                errors.rejectValue("dateOfBirth", CUSTOMER_BIRTH_DATE_IS_IN_THE_FUTURE,
                        CUSTOMER_BIRTH_DATE_IS_IN_THE_FUTURE);
            }
        }

        if (customerRegisterBindingModel.getWeight() < 30){
            errors.rejectValue("weight", WEIGHT_MUST_BE_MORE_THAN_30,
                    WEIGHT_MUST_BE_MORE_THAN_30);
        }

        if (customerRegisterBindingModel.getHeight() < 57){
            errors.rejectValue("height", HEIGHT_MUST_BE_MORE_THAN_57,
                    HEIGHT_MUST_BE_MORE_THAN_57);
        }
    }
}
