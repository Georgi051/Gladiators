package project.gladiators.validators.trainer;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.TrainingPlanToCustomerBindingModel;

import java.time.LocalDate;

import static project.gladiators.constants.validators.TrainerConstants.DATE_IS_IN_THE_PAST;
import static project.gladiators.constants.validators.TrainerConstants.DATE_IS_NULL;

@Validator
public class AddTrainingPlanToCustomerValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return TrainingPlanToCustomerBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TrainingPlanToCustomerBindingModel trainingPlanToCustomer = (TrainingPlanToCustomerBindingModel) o;


        if(trainingPlanToCustomer.getStartedOn() == null){
            errors.rejectValue("startedOn", DATE_IS_NULL,
                    DATE_IS_NULL);
        }
        if(errors.hasErrors()){
            return;
        }
        if(trainingPlanToCustomer.getStartedOn()
        .isBefore(LocalDate.now())){
            errors.rejectValue("startedOn", DATE_IS_IN_THE_PAST,
                    DATE_IS_IN_THE_PAST);
        }

    }
}
