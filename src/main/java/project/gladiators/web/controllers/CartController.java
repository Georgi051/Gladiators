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
import project.gladiators.service.CartService;
import project.gladiators.service.OrderService;
import project.gladiators.service.ProductService;
import project.gladiators.service.serviceModels.OrderServiceModel;
import project.gladiators.web.viewModels.OrderProductViewModel;
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
    private final ModelMapper modelMapper;

    @Autowired
    public CartController(CartService cartService, ProductService productService, OrderService orderService, ModelMapper modelMapper) {
        this.cartService = cartService;
        this.productService = productService;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add-product")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addToCartConfirm(String id, int quantity, HttpSession session) {
        ProductViewModel product = this.modelMapper
                .map(this.productService.findProductById(id), ProductViewModel.class);

        OrderProductViewModel orderProductViewModel = new OrderProductViewModel();
        orderProductViewModel.setProduct(product);
        orderProductViewModel.setPrice(product.getPrice());

        ShoppingCartViewModel cartItem = new ShoppingCartViewModel();
        cartItem.setProduct(orderProductViewModel);
        cartItem.setQuantity(quantity);

        this.cartService.addItemToCart(cartItem, this.cartService.retrieveCart(session));
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
        this.orderService.createOrder(orderServiceModel);
        session.removeAttribute("shopping-cart");
        return super.redirect("/home");
    }

}
