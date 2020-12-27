package project.gladiators.validators.trainer;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.TrainingPlanBindingModel;

import java.time.LocalDate;

import static project.gladiators.constants.validators.TrainerConstants.DATE_IS_IN_THE_PAST;
import static project.gladiators.constants.validators.TrainerConstants.DATE_IS_NULL;

@Validator
public class AddTrainingPlanValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return TrainingPlanBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TrainingPlanBindingModel trainingPlanBindingModel = (TrainingPlanBindingModel) o;

        if(trainingPlanBindingModel.getStartedOn() == null){
            errors.rejectValue("startedOn", DATE_IS_NULL,
                    DATE_IS_NULL);
        }
        if(errors.hasErrors()){
            return;
        }
        if(trainingPlanBindingModel.getStartedOn().isBefore(LocalDate.now())){
            errors.rejectValue("startedOn", DATE_IS_IN_THE_PAST,
                    DATE_IS_IN_THE_PAST);
        }
    }
}
