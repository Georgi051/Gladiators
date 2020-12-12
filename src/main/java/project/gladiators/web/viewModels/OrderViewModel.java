package project.gladiators.web.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderViewModel extends BaseViewModel {
    private List<OrderProductViewModel> products;
    private UserViewModel customer;
    private BigDecimal totalPrice;
    private LocalDateTime madeOn;
}
