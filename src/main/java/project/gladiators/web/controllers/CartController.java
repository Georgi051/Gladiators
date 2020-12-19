package project.gladiators.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import project.gladiators.annotations.PageTitle;
import project.gladiators.exceptions.MaxProductQuantityInCartException;
import project.gladiators.service.CartService;
import project.gladiators.service.OfferService;
import project.gladiators.service.OrderService;
import project.gladiators.service.ProductService;
import project.gladiators.service.serviceModels.OfferServiceModel;
import project.gladiators.service.serviceModels.OrderServiceModel;
import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.web.viewModels.OrderProductViewModel;
import project.gladiators.web.viewModels.ProductDetailsViewModel;
import project.gladiators.web.viewModels.ProductViewModel;
import project.gladiators.web.viewModels.ShoppingCartViewModel;

import javax.servlet.http.HttpSession;
import java.security.Principal;


@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {
    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;
    private final OfferService offerService;
    private final ModelMapper modelMapper;

    @Autowired
    public CartController(CartService cartService, ProductService productService, OrderService orderService, OfferService offerService, ModelMapper modelMapper) {
        this.cartService = cartService;
        this.productService = productService;
        this.orderService = orderService;
        this.offerService = offerService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add-product")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addToCartConfirm(String id, int quantity, HttpSession session,
                                         ModelAndView modelAndView) {
        
        try{
            this.cartService.addItemToCart(id, quantity, this.cartService.retrieveCart(session));
        }catch (MaxProductQuantityInCartException ex){
            modelAndView.addObject("error", ex.getMessage());
            return super.view("/shop", modelAndView);
        }

        return super.redirect("/shop");
    }


    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Cart Details")
    public ModelAndView cartDetails(ModelAndView modelAndView, HttpSession session) {
        modelAndView.addObject("totalPrice", this.cartService.calcTotal(session));
        return super.view("cart/cart-details", modelAndView);
    }


    @PostMapping("/remove-product")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView removeFromCartConfirm(String id, HttpSession session) {
        this.cartService.removeItemFromCart(id,session);
        return super.redirect("/cart/details");
    }

    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView checkoutConfirm(HttpSession session,Principal principal) {
        OrderServiceModel orderServiceModel = this.cartService.prepareOrder(session,principal.getName());
        if (orderServiceModel == null){
            return super.redirect("/home");
        }
        this.orderService.createOrder(orderServiceModel);
        session.removeAttribute("shopping-cart");
        return super.redirect("/home");
    }

}
