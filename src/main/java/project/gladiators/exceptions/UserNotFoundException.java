package project.gladiators.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static project.gladiators.constants.ExceptionMessages.USER_NOT_FOUND;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = USER_NOT_FOUND)
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
