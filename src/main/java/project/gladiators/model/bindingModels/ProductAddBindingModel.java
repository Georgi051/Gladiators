package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.model.entities.Category;
import project.gladiators.model.entities.SubCategory;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProductAddBindingModel {

    private String name;
    private String manufacturerName;
    private String description;
    private Integer quantity;
    private Integer buyingCounter;
    private BigDecimal price;
    private MultipartFile image;
    @NotNull(message = "You must select sub-category.")
    private SubCategory subCategories;
}
