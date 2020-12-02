package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderServiceModel extends BaseServiceModel {
    private List<ProductServiceModel> products;
    private UserServiceModel customer;
    private BigDecimal totalPrice;
    private LocalDateTime madeOn;
}
