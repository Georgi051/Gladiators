package project.gladiators.web.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Product extends BaseViewModel {
    private String name;
    private String manufacturerName;
    private BigDecimal price;
    private Integer quantity;
    private String description;
    private String imageUrl;
    private Integer buyingCounter;
    private MultipartFile newImage;
}
