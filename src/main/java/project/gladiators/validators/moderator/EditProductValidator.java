package project.gladiators.validators.moderator;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.ProductEditBindingModel;

import java.math.BigDecimal;

import static project.gladiators.constants.validators.ModeratorConstants.*;
import static project.gladiators.constants.validators.ModeratorConstants.PRODUCT_DESCRIPTION_LENGTH;

@Validator
public class EditProductValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return ProductEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductEditBindingModel productEditBindingModel = (ProductEditBindingModel) o;

        if (productEditBindingModel.getName().length() < 3){
            errors.rejectValue("name", PRODUCT_NAME_LENGTH_MUST_BE_MORE_THAN_3_SYMBOLS,
                    PRODUCT_NAME_LENGTH_MUST_BE_MORE_THAN_3_SYMBOLS);
        }

        if(productEditBindingModel.getManufacturerName().length() < 3){
            errors.rejectValue("manufacturerName", MANUFACTURER_NAME_LENGTH_MUST_BE_MORE_THAN_3_SYMBOLS,
                    MANUFACTURER_NAME_LENGTH_MUST_BE_MORE_THAN_3_SYMBOLS);
        }

        if(productEditBindingModel.getDescription().length() < 30 ||
                productEditBindingModel.getDescription().length() > 1000){
            errors.rejectValue("description", PRODUCT_DESCRIPTION_LENGTH,
                    PRODUCT_DESCRIPTION_LENGTH);
        }

        if(productEditBindingModel.getQuantity() == null){
            errors.rejectValue("quantity", QUANTITY_CANNOT_BE_EMPTY,
                    QUANTITY_CANNOT_BE_EMPTY);
        }else {
            if(productEditBindingModel.getQuantity() < 1){
                errors.rejectValue("quantity", QUANTITY_CANNOT_BE_NEGATIVE,
                        QUANTITY_CANNOT_BE_NEGATIVE);
            }
        }

        if(productEditBindingModel.getPrice() == null){
            errors.rejectValue("price", PRICE_CANNOT_BE_NULL,
                    PRICE_CANNOT_BE_NULL);
        }else{
            if(productEditBindingModel.getPrice().compareTo(BigDecimal.ZERO) <= 0){
                errors.rejectValue("price", PRICE_CANNOT_BE_NEGATIVE_OR_ZERO,
                        PRICE_CANNOT_BE_NEGATIVE_OR_ZERO);
            }
        }
    }
}
