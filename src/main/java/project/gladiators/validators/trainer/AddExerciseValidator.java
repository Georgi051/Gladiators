package project.gladiators.validators.trainer;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.ExerciseAddBindingModel;
import project.gladiators.repository.ProductRepository;

import static project.gladiators.constants.validators.TrainerConstants.*;

@Validator
public class AddExerciseValidator implements org.springframework.validation.Validator {
    private final ProductRepository productRepository;

    public AddExerciseValidator(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ExerciseAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        ExerciseAddBindingModel exerciseAddBindingModel = (ExerciseAddBindingModel) o;

        if(exerciseAddBindingModel.getName().length() < 3){
            errors.rejectValue("name", EXERCISE_NAME_CANNOT_BE_LESS_THAN_3_SYMBOLS,
                    EXERCISE_NAME_CANNOT_BE_LESS_THAN_3_SYMBOLS);
        }
        if(exerciseAddBindingModel.getDescription().length() < 30 ||
                exerciseAddBindingModel.getDescription().length() > 1000){
            errors.rejectValue("description", EXERCISE_DESCRIPTION_LENGTH,
                    EXERCISE_DESCRIPTION_LENGTH);
        }
    }
}
