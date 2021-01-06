package project.gladiators.init;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.gladiators.constants.GlobalConstants;
import project.gladiators.model.dtos.SubCategoryDto;
import project.gladiators.model.entities.SubCategory;
import project.gladiators.repository.SubCategoryRepository;

import java.io.FileReader;
import java.util.Arrays;

@Component
@Order(value = 7)
public class SubCategoryInitialization implements CommandLineRunner {
   private SubCategoryRepository subCategoryRepository;
    private Gson gson;

    @Autowired
    public SubCategoryInitialization(SubCategoryRepository subCategoryRepository, Gson gson) {
        this.subCategoryRepository = subCategoryRepository;
        this.gson = gson;
    }

    @Override
    public void run(String... args) throws Exception {
        if (subCategoryRepository.count() == 0) {
            Arrays.stream(this.gson.fromJson(new FileReader(GlobalConstants.SUB_CATEGORIES_FILE_PATH), SubCategoryDto[].class))
                    .forEach(
                            e -> {
                                SubCategory subCategory = new SubCategory();
                                subCategory.setName(e.getName());
                                subCategory.setEmpty(true);
                                subCategoryRepository.save(subCategory);
                            }
                    );
        }
    }
}
