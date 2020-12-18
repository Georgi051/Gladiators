package project.gladiators.web.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductDetailsViewModel extends BaseViewModel{

    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    int quantity;
    private BigDecimal discountedPrice;

}
