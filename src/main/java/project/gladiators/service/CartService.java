package project.gladiators.service;

import project.gladiators.service.serviceModels.CustomerServiceModel;
import project.gladiators.service.serviceModels.OrderServiceModel;
import project.gladiators.web.viewModels.ShoppingCartViewModel;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

public interface CartService  {
    BigDecimal calcTotal(HttpSession session);

    List<ShoppingCartViewModel> retrieveCart(HttpSession session);

    void addItemToCart(String id, int quantity, List<ShoppingCartViewModel> cart, CustomerServiceModel customerId);

    void removeItemFromCart(String id,HttpSession session);

    OrderServiceModel prepareOrder(HttpSession session, String customer);
}
