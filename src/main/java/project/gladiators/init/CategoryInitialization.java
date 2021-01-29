package project.gladiators.init;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.gladiators.constants.GlobalConstants;
import project.gladiators.model.dtos.CategoryDto;
import project.gladiators.model.entities.Category;
import project.gladiators.model.entities.SubCategory;
import project.gladiators.repository.CategoryRepository;
import project.gladiators.repository.SubCategoryRepository;

import java.io.FileReader;
import java.util.*;

@Component
@Order(value = 5)
public class CategoryInitialization implements CommandLineRunner {
    private CategoryRepository categoryRepository;
    private SubCategoryRepository subCategoryRepository;
    private ModelMapper modelMapper;
    private Gson gson;

    @Autowired
    public CategoryInitialization(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository
            , ModelMapper modelMapper, Gson gson) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            Arrays.stream(this.gson.fromJson(new FileReader(GlobalConstants.CATEGORIES_FILE_PATH), CategoryDto[].class))
                    .forEach(c -> {
                                Category category = this.modelMapper.map(c, Category.class);
                                List<SubCategory> subCategories = new ArrayList<>();
                                c.getSubCategories().forEach(
                                        subCategory -> subCategories.add(this.subCategoryRepository.findByName(subCategory.getName()))
                                        );
                                category.setSubCategories(subCategories);
                                categoryRepository.save(category);
                      });
        }
    }
}
