package project.gladiators.exceptions;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(String message) {
        super(message);
    }
}
