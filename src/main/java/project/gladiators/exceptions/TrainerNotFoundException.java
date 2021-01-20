package project.gladiators.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static project.gladiators.constants.ExceptionMessages.TRAINER_NOT_FOUND;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = TRAINER_NOT_FOUND)
public class TrainerNotFoundException  extends RuntimeException{
    public TrainerNotFoundException(String message) {
        super(message);
    }
}
