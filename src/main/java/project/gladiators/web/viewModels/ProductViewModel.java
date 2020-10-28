package project.gladiators.web.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductViewModel extends BaseViewModel {
    private String name;
    private String manufacturerName;
    private BigDecimal price;
    private Integer quantity;
    private String description;
    private String imageUrl;
    private Integer buyingCounter;
}