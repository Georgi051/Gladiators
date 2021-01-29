package project.gladiators.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.gladiators.model.entities.Product;
import project.gladiators.repository.ProductRepository;

import java.math.BigDecimal;

@Component
@Order(value = 6)
public class ProductInitialization implements CommandLineRunner {
    private ProductRepository productRepository;

    public ProductInitialization(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (this.productRepository.count() == 0){
            Product product = new Product();
            product.setName("Training plan");
            product.setPrice(BigDecimal.valueOf(25));
            product.setImageUrl("https://res.cloudinary.com/gladiators/image/upload/v1609761974/lost-weight_slx9ev.jpg");
            product.setQuantity(Integer.MAX_VALUE);
            product.setDescription("Training plan description");
            product.setBuyingCounter(0);
            productRepository.save(product);
        }
    }

}
