package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderProductServiceModel extends BaseServiceModel {

    private ProductServiceModel product;
    private BigDecimal price;

}
