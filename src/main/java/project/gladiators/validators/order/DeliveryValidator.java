package project.gladiators.validators.order;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.DeliveryBindingModel;

import static project.gladiators.constants.validators.DeliveryConstants.*;

@Validator
public class DeliveryValidator  implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return DeliveryBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DeliveryBindingModel deliveryBindingModel = (DeliveryBindingModel) o;


        if (deliveryBindingModel.getFirstName().isEmpty()){
            errors.rejectValue("firstName", FIRST_NAME_CANNOT_BE_LESS_THAN_ONE_CHARACTER,
                    FIRST_NAME_CANNOT_BE_LESS_THAN_ONE_CHARACTER);
        }

        if(deliveryBindingModel.getFirstName().contains(" ")){
            errors.rejectValue("firstName", WHITESPACE_IS_NOT_VALID,
                    WHITESPACE_IS_NOT_VALID);
        }

        if (deliveryBindingModel.getLastName().isEmpty()){
            errors.rejectValue("lastName", LAST_NAME_CANNOT_BE_LESS_THAN_ONE_CHARACTER,
                    LAST_NAME_CANNOT_BE_LESS_THAN_ONE_CHARACTER);
        }

        if(deliveryBindingModel.getLastName().contains(" ")){
            errors.rejectValue("lastName", WHITESPACE_IS_NOT_VALID,
                    WHITESPACE_IS_NOT_VALID);
        }

        if (deliveryBindingModel.getAddress().isEmpty()){
            errors.rejectValue("address", ADDRESS_CANNOT_BE_LESS_THAN_ONE_CHARACTER,
                    ADDRESS_CANNOT_BE_LESS_THAN_ONE_CHARACTER);
        }

        if(deliveryBindingModel.getAddress().startsWith(" ")){
            errors.rejectValue("address", WHITESPACE_IS_NOT_VALID,
                    WHITESPACE_IS_NOT_VALID);
        }

        if (deliveryBindingModel.getEmail().isEmpty()){
            errors.rejectValue("email", EMAIL_CANNOT_BE_LESS_THAN_ONE_CHARACTER,
                    EMAIL_CANNOT_BE_LESS_THAN_ONE_CHARACTER);
        }

        if(deliveryBindingModel.getEmail().contains(" ")){
            errors.rejectValue("email", WHITESPACE_IS_NOT_VALID,
                    WHITESPACE_IS_NOT_VALID);
        }

        if(deliveryBindingModel.getNeighborhood().contains(" ")){
            errors.rejectValue("neighborhood", WHITESPACE_IS_NOT_VALID,
                    WHITESPACE_IS_NOT_VALID);
        }

        if(deliveryBindingModel.getCity().isEmpty()){
            errors.rejectValue("city", CITY_CANNOT_BE_LESS_THAN_ONE_CHARACTER,
                    CITY_CANNOT_BE_LESS_THAN_ONE_CHARACTER);
        }

        if(deliveryBindingModel.getCity().length() < 3){
            errors.rejectValue("city", INVALID_CITY_LENGTH,
                    INVALID_CITY_LENGTH);
        }

        if(deliveryBindingModel.getCity().contains(" ")){
            errors.rejectValue("city", WHITESPACE_IS_NOT_VALID,
                    WHITESPACE_IS_NOT_VALID);
        }

        if (deliveryBindingModel.getZip() < 1000 || deliveryBindingModel.getZip() > 10000){
            errors.rejectValue("zip", INVALID_ZIP_CODE_LENGTH,
                    INVALID_ZIP_CODE_LENGTH);
        }

    }
}
