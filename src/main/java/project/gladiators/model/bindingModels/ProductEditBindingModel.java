package project.gladiators.model.bindingModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductEditBindingModel {
    private String name;
    private String manufacturerName;
    private String description;
    private Integer quantity;
    private Integer buyingCounter;
    private BigDecimal price;
    private String imageUrl;
    private MultipartFile newImage;
}
