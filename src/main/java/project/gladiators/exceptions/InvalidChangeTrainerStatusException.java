package project.gladiators.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED, reason = "Invalid change trainer status")
public class InvalidChangeTrainerStatusException extends RuntimeException{
    public InvalidChangeTrainerStatusException(String message) {
        super(message);
    }
}
