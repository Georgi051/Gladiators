package project.gladiators.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static project.gladiators.constants.ExceptionMessages.ARTICLE_NOT_FOUND;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = ARTICLE_NOT_FOUND)
public class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException() {
    }

    public ArticleNotFoundException(String message) {
        super(message);
    }
}
