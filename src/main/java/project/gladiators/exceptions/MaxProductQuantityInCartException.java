package project.gladiators.exceptions;

public class MaxProductQuantityInCartException extends RuntimeException{

    public MaxProductQuantityInCartException(String message){
       super(message);
    }
}
