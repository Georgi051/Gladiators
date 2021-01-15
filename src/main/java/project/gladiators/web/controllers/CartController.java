package project.gladiators.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.exceptions.MaxProductQuantityInCartException;
import project.gladiators.service.*;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.CustomerTrainingPlanInfoServiceModel;
import project.gladiators.service.serviceModels.OrderServiceModel;

import javax.servlet.http.HttpSession;
import java.security.Principal;


@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {
    private final CartService cartService;
    private final UserService userService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final CustomerTrainingPlanInfoService customerTrainingPlanInfoService;

    @Autowired
    public CartController(CartService cartService, UserService userService, CustomerService customerService, ProductService productService, CustomerTrainingPlanInfoService customerTrainingPlanInfoService) {
        this.cartService = cartService;
        this.userService = userService;
        this.customerService = customerService;
        this.productService = productService;
        this.customerTrainingPlanInfoService = customerTrainingPlanInfoService;
    }

    @PostMapping("/add-product")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addToCartConfirm(String id, int quantity, HttpSession session,
                                         ModelAndView modelAndView) {

        try{
            this.cartService.addItemToCart(id, quantity, this.cartService.retrieveCart(session), null);
        }catch (MaxProductQuantityInCartException ex){
            modelAndView.addObject("error", ex.getMessage());
            return super.view("/shop", modelAndView);
        }

        return super.redirect("/shop");
    }

    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Cart Details")
    public ModelAndView cartDetails(ModelAndView modelAndView, HttpSession session , Principal principal) {
        CustomerServiceModel customerServiceModel = customerService.findCustomerByUser(userService.findUserByUsername(principal.getName()));
        CustomerTrainingPlanInfoServiceModel customerTrainingPlan = this.customerTrainingPlanInfoService.findByCustomer(customerServiceModel);

        if (customerTrainingPlan != null && !customerTrainingPlan.isPaid()){
            this.cartService.addItemToCart(productService.findByName("Training plan").getId(),1,this.cartService.retrieveCart(session),customerServiceModel);
        }
        modelAndView.addObject("totalPrice", this.cartService.calcTotal(session));
        return super.view("cart/cart-details", modelAndView);
    }

    @PostMapping("/remove-product")
    @PageTitle("Cart Details")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView removeFromCartConfirm(String id, HttpSession session) {
        this.cartService.removeItemFromCart(id,session);
        return super.redirect("/cart/details");
    }

    @GetMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Order Details")
    public ModelAndView getCheckout(HttpSession session, ModelAndView modelAndView,
                                    Principal principal){

        modelAndView.addObject("totalPrice", this.cartService.calcTotal(session));
        OrderServiceModel orderServiceModel = this.cartService.prepareOrder(session,principal.getName());
        modelAndView.addObject("order", orderServiceModel);
        return super.view("cart/checkout", modelAndView);

    }

}
