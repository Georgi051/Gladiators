package project.gladiators.web.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.service.CartService;
import project.gladiators.service.OrderService;
import project.gladiators.service.PaypalService;
import project.gladiators.service.serviceModels.OrderServiceModel;

import javax.servlet.http.HttpSession;
import java.security.Principal;

import static project.gladiators.constants.PayPalConstants.*;
import static project.gladiators.constants.ShoppingCartConstants.SHOPPING_CART;

@Controller
@RequestMapping("/payment")
public class PayPalController extends BaseController {

    private final PaypalService service;
    private final CartService cartService;
    private final OrderService orderService;

    public PayPalController(PaypalService service, CartService cartService, OrderService orderService) {
        this.service = service;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @PostMapping("/pay")
    @PreAuthorize("isAuthenticated()")
    public String payment(HttpSession session, Principal principal) {
        OrderServiceModel order = this.cartService.prepareOrder(session, principal.getName());
        try {
            StringBuilder stringBuilder = new StringBuilder();
            order.getProducts().forEach(orderProduct -> stringBuilder.append(orderProduct.getProduct().getName())
                    .append(": ").append(PRICE).append(String.format(PRICE_FORMAT, orderProduct.getPrice()))
                    .append(QUANTITY).append(orderProduct.getQuantity())
                    .append("\r\n"));
            Payment payment = service.createPayment(order.getTotalPrice().doubleValue(),
                    CURRENCY, METHOD,
                    INTENT, stringBuilder.toString(), CANCEL_URL,
                    SUCCESS_URL);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals(APPROVAL_URL)) {
                    return "redirect:" + link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/cancel")
    @PreAuthorize("isAuthenticated()")
    public String cancelPay(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("statusMessage", CANCELED_MASSAGE);
        redirectAttributes.addFlashAttribute("statusCode", "error");
        return "redirect:/home";
    }

    @GetMapping("/success")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
                                   HttpSession session, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            if (payment.getState().equals(APPROVED)) {
                OrderServiceModel orderServiceModel = this.cartService.prepareOrder(session, principal.getName());
                this.orderService.createOrder(orderServiceModel,principal.getName());
                redirectAttributes.addFlashAttribute("statusMessage", SUCCESSFUL_MASSAGE);
                redirectAttributes.addFlashAttribute("statusCode", "successful");
                session.removeAttribute(SHOPPING_CART);
                return super.redirect("/");
            }
        } catch (PayPalRESTException e) {
            redirectAttributes.addFlashAttribute("statusMessage", e.getDetails().getMessage());
            redirectAttributes.addFlashAttribute("statusCode", "error");
            return super.redirect("/");
        }
        return super.redirect("/");
    }

}
