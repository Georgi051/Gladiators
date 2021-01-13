package project.gladiators.web.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.service.CartService;
import project.gladiators.service.OrderService;
import project.gladiators.service.PaypalService;
import project.gladiators.service.serviceModels.OrderServiceModel;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/payment")
public class PaypalController extends BaseController{

    private final PaypalService service;
    private final CartService cartService;
    private final OrderService orderService;

    public PaypalController(PaypalService service, CartService cartService, OrderService orderService) {
        this.service = service;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @PostMapping("/pay")
    @PreAuthorize("isAuthenticated()")
    public String payment(HttpSession session, Principal principal) {
        OrderServiceModel order = this.cartService.prepareOrder(session,principal.getName());
        try {
            StringBuilder stringBuilder = new StringBuilder();
            order.getProducts().forEach(orderProduct -> {
                stringBuilder.append(orderProduct.getProduct().getName())
                        .append(": ").append("price: ").append(String.format("%.2f ", orderProduct.getPrice()))
                        .append("quantity: ").append(orderProduct.getQuantity())
                        .append("\r\n");
            });
            Payment payment = service.createPayment(order.getTotalPrice().doubleValue(),
                    "USD", "Paypal",
                    "sale", stringBuilder.toString(), "http://localhost:8080/payment/cancel",
                    "http://localhost:8080/payment/success");
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
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
        redirectAttributes.addFlashAttribute("statusMessage", "Your order is canceled!");
        redirectAttributes.addFlashAttribute("statusCode", "error");
        return "redirect:/home";
    }

    @GetMapping("/success")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
                                   HttpSession session, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                OrderServiceModel orderServiceModel = this.cartService.prepareOrder(session,principal.getName());
                this.orderService.createOrder(orderServiceModel);
                redirectAttributes.addFlashAttribute("statusMessage", "Your order was successfully made!");
                redirectAttributes.addFlashAttribute("statusCode", "successful");
                session.removeAttribute("shopping-cart");
                return super.redirect("/");
            }
        } catch (PayPalRESTException e) {
            redirectAttributes.addFlashAttribute("statusMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("statusCode", "error");
            return super.redirect("/");
        }
        return super.redirect("/");
    }

}
