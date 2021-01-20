package project.gladiators.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static project.gladiators.constants.ExceptionMessages.PRODUCT_NOT_FOUND;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = PRODUCT_NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
