package project.gladiators.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static project.gladiators.constants.ExceptionMessages.SUBCATEGORY_NOT_FOUND;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = SUBCATEGORY_NOT_FOUND)
public class SubCategoryNotFoundException extends RuntimeException {

    public SubCategoryNotFoundException(String massage) {
        super(massage);
    }
}
