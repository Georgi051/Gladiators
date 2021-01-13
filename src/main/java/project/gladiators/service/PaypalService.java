package project.gladiators.service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import project.gladiators.service.serviceModels.OrderServiceModel;

public interface PaypalService {

    Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException;
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
