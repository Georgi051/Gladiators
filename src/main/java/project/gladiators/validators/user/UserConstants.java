package project.gladiators.validators.user;

public class UserConstants {

    final static String USERNAME_CANNOT_BE_EMPTY = "Please enter username!";
    final static String USERNAME_ALREADY_EXIST = "This username already exist!";
    final static String EMAIL_ALREADY_EXIST = "This email already exist!";
    final static String USERNAME_IS_NOT_VALID = "You cannot use whitespaces for username!";
    final static String USERNAME_LENGTH_IS_NOT_VALID = "Username must be at least 4 characters!";
    final static String PASSWORD_CANNOT_BE_EMPTY = "Please enter password!";
    final static String PASSWORDS_DO_NOT_MATCH = "Passwords are not equal!";
    final static String PASSWORD_NOT_VALID = "Password should be with one upper case, one lower case, one digit[0-9], \n" +
            "   one special character[#?!@$%~^&*-] and the minimum length should be more than 7.";

    final static String FIRST_NAME_LENGTH_IS_INVALID = "Please enter first name!";
    final static String LAST_NAME_LENGTH_IS_INVALID = "Please enter last name!";
    final static String OLD_PASSWORD_ERROR = "This is not your password!";
}
