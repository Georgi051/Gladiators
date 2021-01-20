package project.gladiators.validators.moderator;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.ProductAddBindingModel;
import project.gladiators.repository.ProductRepository;

import java.math.BigDecimal;

import static project.gladiators.constants.validators.ModeratorConstants.*;

@Validator
public class AddProductValidator implements org.springframework.validation.Validator {

    private final ProductRepository productRepository;

    public AddProductValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ProductAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        ProductAddBindingModel productAddBindingModel = (ProductAddBindingModel) o;

        if (productRepository.findByName(productAddBindingModel.getName()) != null) {
            errors.rejectValue("name", PRODUCT_NAME_ALREADY_EXIST,
                    PRODUCT_NAME_ALREADY_EXIST);
            if (productAddBindingModel.getName().length() < 3) {
                errors.rejectValue("name", PRODUCT_NAME_LENGTH_MUST_BE_MORE_THAN_3_SYMBOLS,
                        PRODUCT_NAME_LENGTH_MUST_BE_MORE_THAN_3_SYMBOLS);
            }
        }

        if (errors.hasErrors()) {
            return;
        }

        if (productAddBindingModel.getManufacturerName().length() < 3) {
            errors.rejectValue("manufacturerName", MANUFACTURER_NAME_LENGTH_MUST_BE_MORE_THAN_3_SYMBOLS,
                    MANUFACTURER_NAME_LENGTH_MUST_BE_MORE_THAN_3_SYMBOLS);
        }
        if (productAddBindingModel.getDescription().length() < 30 ||
                productAddBindingModel.getDescription().length() > 1000) {
            errors.rejectValue("description", PRODUCT_DESCRIPTION_LENGTH,
                    PRODUCT_DESCRIPTION_LENGTH);
        }

        if (productAddBindingModel.getPrice() == null) {
            errors.rejectValue("price", PRICE_CANNOT_BE_NULL,
                    PRICE_CANNOT_BE_NULL);
        } else {
            if (productAddBindingModel.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                errors.rejectValue("price", PRICE_CANNOT_BE_NEGATIVE_OR_ZERO,
                        PRICE_CANNOT_BE_NEGATIVE_OR_ZERO);
            }
        }
        if (productAddBindingModel.getQuantity() == null) {
            errors.rejectValue("quantity", QUANTITY_CANNOT_BE_EMPTY,
                    QUANTITY_CANNOT_BE_EMPTY);
        } else {
            if (productAddBindingModel.getQuantity() < 1) {
                errors.rejectValue("quantity", QUANTITY_CANNOT_BE_NEGATIVE,
                        QUANTITY_CANNOT_BE_NEGATIVE);
            }
        }

    }
}
