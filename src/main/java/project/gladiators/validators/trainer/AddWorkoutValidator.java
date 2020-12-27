package project.gladiators.validators.trainer;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.WorkoutAddBindingModel;

import static project.gladiators.constants.validators.TrainerConstants.*;

@Validator
public class AddWorkoutValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return WorkoutAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        WorkoutAddBindingModel workoutAddBindingModel = (WorkoutAddBindingModel) o;

        if(workoutAddBindingModel.getName().length() < 3){
            errors.rejectValue("name", WORKOUT_NAME_CANNOT_BE_LESS_THAN_3_SYMBOLS,
                    WORKOUT_NAME_CANNOT_BE_LESS_THAN_3_SYMBOLS);
        }

        if(workoutAddBindingModel.getExercises() == null){
            errors.rejectValue("exercises", EXERCISES_CANNOT_BE_NULL,
                    EXERCISES_CANNOT_BE_NULL);
        }
        else {
             if(workoutAddBindingModel.getRestTime().size() < workoutAddBindingModel.getExercises().size() ||
             workoutAddBindingModel.getSets().size() < workoutAddBindingModel.getExercises().size() ||
             workoutAddBindingModel.getRepeats().size() < workoutAddBindingModel.getExercises().size()){
                 errors.rejectValue("exercises", EXERCISES_INFO_IS_NOT_FULL,
                         EXERCISES_INFO_IS_NOT_FULL);
             }

        }

    }
}
