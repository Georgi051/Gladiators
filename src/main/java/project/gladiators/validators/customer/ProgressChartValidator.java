package project.gladiators.validators.customer;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.ProgressChartEditBindingModel;

import static project.gladiators.constants.validators.CustomerConstants.*;

@Validator
public class ProgressChartValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return ProgressChartEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProgressChartEditBindingModel progressChartEditBindingModel = (ProgressChartEditBindingModel) o;

        if (progressChartEditBindingModel.getWeight() < 30){
            errors.rejectValue("weight", WEIGHT_MUST_BE_MORE_THAN_30,
                    WEIGHT_MUST_BE_MORE_THAN_30);
        }

        if (progressChartEditBindingModel.getHeight() < 57){
            errors.rejectValue("height", HEIGHT_MUST_BE_MORE_THAN_57,
                    HEIGHT_MUST_BE_MORE_THAN_57);
        }

        if (progressChartEditBindingModel.getChest() < 0){
            errors.rejectValue("chest",CHEST_MUST_BE_POSITIVE,
                    CHEST_MUST_BE_POSITIVE);
        }

        if (progressChartEditBindingModel.getWaist() < 0){
            errors.rejectValue("waist",WAIST_MUST_BE_POSITIVE,
                    WAIST_MUST_BE_POSITIVE);
        }

        if (progressChartEditBindingModel.getBiceps() < 0){
            errors.rejectValue("biceps",BICEPS_MUST_BE_POSITIVE,
                    BICEPS_MUST_BE_POSITIVE);
        }

        if (progressChartEditBindingModel.getThigh() < 0){
            errors.rejectValue("thigh",THIGH_MUST_BE_POSITIVE,
                    THIGH_MUST_BE_POSITIVE);
        }

    }
}
