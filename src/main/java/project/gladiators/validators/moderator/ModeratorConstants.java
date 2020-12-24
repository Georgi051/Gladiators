package project.gladiators.validators.moderator;

public class ModeratorConstants {

    /*ARTICLE ADD CONSTANTS*/
    final static String ARTICLE_TITLE_LENGTH = "The title must be between 8 and 40 characters!";
    final static String ARTICLE_DESCRIPTION_LENGTH = "The description must be between 50 and 2000 characters!";
    final static String ARTICLE_IMAGE_CANNOT_BE_NULL= "You need to upload a photo!";
    /*CATEGORY ADD CONSTANTS*/
    final static String CATEGORY_NAME_LENGTH = "The category must be between 4 and 20 characters!";
    /*PRODUCT ADD CONSTANTS*/
    final static String PRODUCT_NAME_LENGTH_MUST_BE_MORE_THAN_3_SYMBOLS = "Product name cannot be less than 3 symbols!";
    final static String MANUFACTURER_NAME_LENGTH_MUST_BE_MORE_THAN_3_SYMBOLS = "Manufacturer name cannot be less than 3 symbols!";
    final static String PRODUCT_DESCRIPTION_LENGTH = "The description must be between 30 and 1000 characters!";
    final static String PRICE_CANNOT_BE_NEGATIVE_OR_ZERO = "Price can not be negative or zero!";
    final static String PRICE_CANNOT_BE_NULL = "Please enter price!";
    final static String QUANTITY_CANNOT_BE_NEGATIVE = "Quantity cannot be negative!";
    final static String QUANTITY_CANNOT_BE_EMPTY = "Quantity cannot be empty!";
}
