package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingCartServiceModel extends BaseServiceModel{

    private ProductServiceModel product;
    private int quantity;
}
