package project.gladiators.constants;

public class PayPalConstants {
    /*StringBuilder constants*/
    public static final String PRICE = "price: ";
    public static final String PRICE_FORMAT = "%.2f ";
    public static final String QUANTITY = "quantity: ";

    /*Payment constants*/
    public static final String CURRENCY = "USD";
    public static final String METHOD = "Paypal";
    public static final String INTENT = "sale";
    public static final String CANCEL_URL = "http://localhost:8080/payment/cancel";
    public static final String SUCCESS_URL = "http://localhost:8080/payment/success";
    public static final String APPROVAL_URL = "approval_url";
    public static final String APPROVED = "approved";
    public static final String SUCCESSFUL_MASSAGE = "Your order was successfully made!";
    public static final String CANCELED_MASSAGE = "Your order is canceled!";

}
