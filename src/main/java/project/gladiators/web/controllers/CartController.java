package project.gladiators.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.annotations.PageTitle;
import project.gladiators.exceptions.MaxProductQuantityInCartException;
import project.gladiators.model.bindingModels.DeliveryBindingModel;
import project.gladiators.service.*;
import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.CustomerTrainingPlanInfoServiceModel;
import project.gladiators.service.serviceModels.OrderServiceModel;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static project.gladiators.constants.ShoppingCartConstants.NO_PRODUCT;
import static project.gladiators.constants.ShoppingCartConstants.SHOPPING_CART;


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
    @PageTitle("Cart Details")
    public ModelAndView cartDetails(ModelAndView modelAndView, HttpSession session , Principal principal) {
        CustomerServiceModel customerServiceModel = customerService.findCustomerByUser(userService.findUserByUsername(principal.getName()));
        if(customerServiceModel != null){

        CustomerTrainingPlanInfoServiceModel customerTrainingPlan = this.customerTrainingPlanInfoService.findByCustomer(customerServiceModel);

        if (customerTrainingPlan != null && !customerTrainingPlan.isPaid()){
            this.cartService.addItemToCart(productService.findByName("Training plan").getId(),1,this.cartService.retrieveCart(session),customerServiceModel);
        }

        }
        modelAndView.addObject("totalPrice", this.cartService.calcTotal(session));
        return super.view("cart/cart-details", modelAndView);
    }

    @PostMapping("/remove-product")
    @PageTitle("Cart Details")
    public ModelAndView removeFromCartConfirm(String id, HttpSession session) {
        this.cartService.removeItemFromCart(id,session);
        return super.redirect("/cart/details");
    }

    @GetMapping("/checkout")
    @PageTitle("Order Details")
    public ModelAndView getCheckout(@ModelAttribute(name = "delivery") DeliveryBindingModel delivery, HttpSession session, ModelAndView modelAndView,
                                    Principal principal, RedirectAttributes redirectAttributes){
        List<String> shoppingCard = (List<String>) session.getAttribute(SHOPPING_CART);
        if (shoppingCard.size() == 0){
            redirectAttributes.addFlashAttribute("statusMessage", NO_PRODUCT);
            redirectAttributes.addFlashAttribute("statusCode", "error");
            return super.redirect("/home");
        }
        modelAndView.addObject("delivery", delivery);
        modelAndView.addObject("totalPrice", this.cartService.calcTotal(session));
        OrderServiceModel orderServiceModel = this.cartService.prepareOrder(session,principal.getName());
        modelAndView.addObject("order", orderServiceModel);
        boolean hasTrainingPlan = checkForTrainingPlan(orderServiceModel);
        modelAndView.addObject("hasTrainingPlan", hasTrainingPlan);
        return super.view("cart/checkout", modelAndView);

    }

    private boolean checkForTrainingPlan(OrderServiceModel orderServiceModel) {
        AtomicBoolean containsTrainingPlan = new AtomicBoolean(false);
        orderServiceModel.getProducts().forEach(orderProductServiceModel -> {
               if(orderProductServiceModel.getProduct().getName().equals("Training plan")){
                   containsTrainingPlan.set(true);
               }
                }
        );
        return containsTrainingPlan.get();
    }

}
