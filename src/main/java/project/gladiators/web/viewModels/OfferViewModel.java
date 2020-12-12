package project.gladiators.web.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OfferViewModel extends BaseViewModel {

    private ProductDetailsViewModel product;
    private BigDecimal price;

}
