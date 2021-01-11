package project.gladiators.validators.trainer;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.TrainingPlanBindingModel;

import java.time.LocalDate;

import static project.gladiators.constants.validators.TrainerConstants.*;

@Validator
public class AddTrainingPlanValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return TrainingPlanBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TrainingPlanBindingModel trainingPlanBindingModel = (TrainingPlanBindingModel) o;

        if (trainingPlanBindingModel.getName().length() < 3){
            errors.rejectValue("name",NAME_LENGTH,
                    NAME_LENGTH);
        }
    }
}
