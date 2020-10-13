package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProductServiceModel extends BaseServiceModel{
    private String name;
    private String manufacturerName;
    private BigDecimal price;
    private Integer quantity;
    private Integer buyingCounter;
    private String description;
    private String imageUrl;
    private Set<CategoryServiceModel> categories;
    private List<ReviewServiceModel> reviews;
}
