package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.exceptions.MaxProductQuantityInCartException;
import project.gladiators.service.CartService;
import project.gladiators.service.OfferService;
import project.gladiators.service.ProductService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.OfferServiceModel;
import project.gladiators.service.serviceModels.OrderProductServiceModel;
import project.gladiators.service.serviceModels.OrderServiceModel;
import project.gladiators.web.viewModels.OrderProductViewModel;
import project.gladiators.web.viewModels.ProductDetailsViewModel;
import project.gladiators.web.viewModels.ShoppingCartViewModel;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final OfferService offerService;
    private final ProductService productService;

    @Autowired
    public CartServiceImpl(UserService userService, ModelMapper modelMapper, OfferService offerService, ProductService productService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.offerService = offerService;
        this.productService = productService;
    }

    @Override
    public List<ShoppingCartViewModel> retrieveCart(HttpSession session) {
        this.initCart(session);
        return (List<ShoppingCartViewModel>) session.getAttribute("shopping-cart");
    }

    private void initCart(HttpSession session) {
        if (session.getAttribute("shopping-cart") == null) {
            session.setAttribute("shopping-cart", new LinkedList<>());
        }
    }

    @Override
    public void addItemToCart(String id, int quantity, List<ShoppingCartViewModel> cart) {
        ProductDetailsViewModel product = this.modelMapper
                .map(this.productService.findProductById(id), ProductDetailsViewModel.class);
        OfferServiceModel offerServiceModel = this.offerService
                .findByProductId(id);

        if(quantity > product.getQuantity()){
            throw new MaxProductQuantityInCartException(
                    "We are sorry but we don't have the amount of the product you wants! Try to order smaller amount!");
        }
        OrderProductViewModel orderProductViewModel = new OrderProductViewModel();
        orderProductViewModel.setProduct(product);
        orderProductViewModel.setPrice(product.getPrice());
        ShoppingCartViewModel item = new ShoppingCartViewModel();
        item.setProduct(orderProductViewModel);
        item.setQuantity(quantity);
        if(offerServiceModel != null){
            item.getProduct().setPrice(offerServiceModel.getPrice());
        }
        for (ShoppingCartViewModel shoppingCartItem : cart) {
            if (shoppingCartItem.getProduct().getProduct().getId().equals(item.getProduct().getProduct().getId())) {

                if(shoppingCartItem.getQuantity() + item.getQuantity() > 15){
                    throw new MaxProductQuantityInCartException("You have reached limit of order product quantity!");
                }

                shoppingCartItem.setQuantity(shoppingCartItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        if(item.getProduct().getProduct().getDescription().length() > 20){
            item.getProduct().getProduct().setDescription
                    (item.getProduct().getProduct().getDescription().substring(0, 19).concat("..."));
        }
        cart.add(item);
    }

    @Override
    public void removeItemFromCart(String id, HttpSession session) {
        List<ShoppingCartViewModel> cart = this.retrieveCart(session);
        cart.removeIf(ci -> ci.getProduct().getProduct().getId().equals(id));
    }

    @Override
    public BigDecimal calcTotal(HttpSession session) {
        List<ShoppingCartViewModel> cart = this.retrieveCart(session);
        BigDecimal result = new BigDecimal(0);
        for (ShoppingCartViewModel item : cart) {
            result = result.add(item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        return result;
    }

    @Override
    public OrderServiceModel prepareOrder(HttpSession session, String customer) {
        OrderServiceModel orderServiceModel = new OrderServiceModel();
        List<ShoppingCartViewModel> shoppingCartViewModels = this.retrieveCart(session);
        List<OrderProductServiceModel> products = new ArrayList<>();
        if (shoppingCartViewModels.size() == 0){
            return null;
        }
        orderServiceModel.setCustomer(this.userService.findUserByUsername(customer));

        for (ShoppingCartViewModel item : shoppingCartViewModels) {
            OrderProductServiceModel orderProductServiceModel =
                    this.modelMapper.map(item.getProduct(), OrderProductServiceModel.class);
            orderProductServiceModel.getProduct().setBuyingProductsQuantity(item.getQuantity());
            orderProductServiceModel.setQuantity(item.getQuantity());
            products.add(orderProductServiceModel);
        }

        orderServiceModel.setProducts(products);
        orderServiceModel.setTotalPrice(this.calcTotal(session));
        orderServiceModel.setMadeOn(LocalDateTime.now());

        return orderServiceModel;
    }
}
