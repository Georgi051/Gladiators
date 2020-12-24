package project.gladiators.validators.moderator;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.CategoryBindingModel;

import static project.gladiators.validators.moderator.ModeratorConstants.CATEGORY_NAME_LENGTH;

@Validator
public class AddCategoryValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CategoryBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CategoryBindingModel categoryBindingModel = (CategoryBindingModel) o;

        if (categoryBindingModel.getName().length() < 4 ||
        categoryBindingModel.getName().length() > 20){
            errors.rejectValue("name",CATEGORY_NAME_LENGTH,CATEGORY_NAME_LENGTH);
        }
    }
}
