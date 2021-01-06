package project.gladiators.validators.moderator;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.SubCategoryBindingModel;

import static project.gladiators.constants.validators.ModeratorConstants.SUBCATEGORY_NAME_LENGTH;

@Validator
public class AddSubCategoryValidator implements org.springframework.validation.Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return SubCategoryBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SubCategoryBindingModel subCategoryBindingModel = (SubCategoryBindingModel) o;

        if (subCategoryBindingModel.getName().length() < 3 ||
                subCategoryBindingModel.getName().length() > 20){
            errors.rejectValue("name",SUBCATEGORY_NAME_LENGTH,SUBCATEGORY_NAME_LENGTH);
        }
    }
}
