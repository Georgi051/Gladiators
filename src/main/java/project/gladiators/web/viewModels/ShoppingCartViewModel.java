package project.gladiators.web.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingCartViewModel extends BaseViewModel implements Serializable {

    private OrderProductViewModel product;
    private int quantity;
}
