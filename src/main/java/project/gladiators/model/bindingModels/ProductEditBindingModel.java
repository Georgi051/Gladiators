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
public class ProductEditBindingModel {
    @Size(min = 3, message = "Exercise name must be at least 3 characters")
    private String name;
    @Size(min = 3, message = "Exercise name must be at least 3 characters")
    private String manufacturerName;
    @Size(min = 30, max = 1000, message = "The description must be between 30 and 1000 characters!")
    private String description;
    @NotNull(message = "Please enter quantity!")
    @Min(value = 1, message = "Quantity cannot be less than 1!")
    private Integer quantity;
    private Integer buyingCounter;
    @DecimalMin(value = "0.01", message = "Price can not be negative!")
    @NotNull(message = "Please enter price!")
    private BigDecimal price;
    @NotNull
    private MultipartFile image;
    @NotNull(message = "You must select sub-category.")
    private SubCategory subCategories;
}
