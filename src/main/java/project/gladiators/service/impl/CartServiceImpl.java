package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.service.CartService;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.OrderServiceModel;
import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.web.viewModels.ShoppingCartViewModel;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public CartServiceImpl(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
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
    public void addItemToCart(ShoppingCartViewModel item, List<ShoppingCartViewModel> cart) {
        for (ShoppingCartViewModel shoppingCartItem : cart) {
            if (shoppingCartItem.getProduct().getProduct().getId().equals(item.getProduct().getProduct().getId())) {
                shoppingCartItem.setQuantity(shoppingCartItem.getQuantity() + item.getQuantity());
                return;
            }
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
        List<ProductServiceModel> products = new ArrayList<>();

        orderServiceModel.setCustomer(this.userService.findUserByUsername(customer));

        for (ShoppingCartViewModel item : shoppingCartViewModels) {
            ProductServiceModel productServiceModel =
                    this.modelMapper.map(item.getProduct().getProduct(), ProductServiceModel.class);
            productServiceModel.setBuyingProductsQuantity(item.getQuantity());
            for (int i = 0; i < item.getQuantity(); i++) {
                products.add(productServiceModel);
            }
        }

        orderServiceModel.setProducts(products);
        orderServiceModel.setTotalPrice(this.calcTotal(session));

        return orderServiceModel;
    }
}
