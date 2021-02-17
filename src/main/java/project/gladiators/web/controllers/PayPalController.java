package project.gladiators.web.controllers;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.model.bindingModels.DeliveryBindingModel;
import project.gladiators.service.CartService;
import project.gladiators.service.OrderService;
import project.gladiators.service.PaypalService;
import project.gladiators.service.serviceModels.DeliveryServiceModel;
import project.gladiators.service.serviceModels.OrderServiceModel;
import project.gladiators.validators.order.DeliveryValidator;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

import static project.gladiators.constants.PayPalConstants.*;
import static project.gladiators.constants.ShoppingCartConstants.SHOPPING_CART;

@Controller
@RequestMapping("/payment")
public class PayPalController extends BaseController {

    private final PaypalService paypalService;
    private final CartService cartService;
    private final OrderService orderService;
    private final DeliveryValidator deliveryValidator;
    private final ModelMapper modelMapper;

    public PayPalController(PaypalService service, CartService cartService, OrderService orderService, DeliveryValidator deliveryValidator, ModelMapper modelMapper) {
        this.paypalService = service;
        this.cartService = cartService;
        this.orderService = orderService;
        this.deliveryValidator = deliveryValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/pay")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView payment(@Valid @ModelAttribute(name = "delivery") DeliveryBindingModel delivery, HttpSession session, Principal principal,
            BindingResult bindingResult,ModelAndView modelAndView) {
        OrderServiceModel order = this.cartService.prepareOrder(session, principal.getName());

        this.deliveryValidator.validate(delivery,bindingResult);
        if(bindingResult.hasErrors()){
            modelAndView.addObject("delivery", delivery);
            modelAndView.addObject("totalPrice", this.cartService.calcTotal(session));
            OrderServiceModel orderServiceModel = this.cartService.prepareOrder(session,principal.getName());
            modelAndView.addObject("order", orderServiceModel);
            return super.view("/cart/checkout", modelAndView);
        }

        DeliveryServiceModel deliveryServiceModel = this.modelMapper.map(delivery, DeliveryServiceModel.class);
        if (delivery.getPayMethod().equals("paypal")){
        session.setAttribute("deliveryServiceModel",deliveryServiceModel);
        try {
            StringBuilder stringBuilder = new StringBuilder();
            order.getProducts().forEach(orderProduct -> stringBuilder.append(orderProduct.getProduct().getName())
                    .append(": ").append(PRICE).append(String.format(PRICE_FORMAT, orderProduct.getPrice()))
                    .append(QUANTITY).append(orderProduct.getQuantity())
                    .append("\r\n"));
            Payment payment = paypalService.createPayment(order.getTotalPrice().doubleValue(),
                    CURRENCY, METHOD,
                    INTENT, stringBuilder.toString(), CANCEL_URL,
                    SUCCESS_URL);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals(APPROVAL_URL)) {
                    return super.redirect(link.getHref());
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
    }else {
        OrderServiceModel orderServiceModel = this.cartService.prepareOrder(session, principal.getName());
        this.orderService.createOrder(orderServiceModel,principal.getName(), deliveryServiceModel);
        session.removeAttribute(SHOPPING_CART);
        
    }
        return super.redirect("/home");
    }

    @GetMapping("/cancel")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView cancelPay(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("statusMessage", CANCELED_MASSAGE);
        redirectAttributes.addFlashAttribute("statusCode", "error");
        return super.redirect("/home");
    }

    @GetMapping("/success")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
                                   HttpSession session, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals(APPROVED)) {
                DeliveryServiceModel deliveryServiceModel = (DeliveryServiceModel)session.getAttribute("deliveryServiceModel");                OrderServiceModel orderServiceModel = this.cartService.prepareOrder(session, principal.getName());
                this.orderService.createOrder(orderServiceModel,principal.getName(), deliveryServiceModel);
                redirectAttributes.addFlashAttribute("statusMessage", SUCCESSFUL_MASSAGE);
                redirectAttributes.addFlashAttribute("statusCode", "successful");
                session.removeAttribute(SHOPPING_CART);
                session.removeAttribute("deliveryServiceModel");
                return super.redirect("/home");
            }
        } catch (PayPalRESTException e) {
            redirectAttributes.addFlashAttribute("statusMessage", e.getDetails().getMessage());
            redirectAttributes.addFlashAttribute("statusCode", "error");
            return super.redirect("/home");
        }
        return super.redirect("/home");
    }

}
