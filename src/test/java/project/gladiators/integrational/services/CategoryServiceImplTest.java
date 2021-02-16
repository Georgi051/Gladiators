package project.gladiators.integrational.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.gladiators.exceptions.CategoryNotFoundException;
import project.gladiators.model.entities.Category;
import project.gladiators.repository.CategoryRepository;
import project.gladiators.service.CategoryService;
import project.gladiators.service.SubCategoryService;
import project.gladiators.service.serviceModels.CategoryServiceModel;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.gladiators.constants.ExceptionMessages.CATEGORY_NOT_FOUND;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Autowired
    CategoryService categoryService;

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    SubCategoryService subCategoryService;

    @Autowired
    ModelMapper modelMapper;

    private Category category;
    private CategoryServiceModel categoryServiceModel;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setName("Proteins");
        category.setId("testId");

        categoryServiceModel = new CategoryServiceModel();
        categoryServiceModel.setName("FatBurner");
    }

    @Test
    void testToSeedCategoryInDb() {
        CategoryServiceModel categoryServiceModel = new CategoryServiceModel();
        categoryServiceModel.setId("1");
        categoryServiceModel.setName("Proteins");

        this.categoryService.seedCategoryInDb(categoryServiceModel);

        verify(categoryRepository).saveAndFlush(any());
    }

    @Test
    void testAllCustomerModelCategories() {
        when(this.categoryRepository.findAll())
                .thenReturn(List.of(category));

        List<CategoryServiceModel> categoryList = this.categoryService.allCategories();
        CategoryServiceModel testCategory = categoryList.get(0);

        Assertions.assertEquals(1, categoryList.size());
        Assertions.assertEquals(category.getName(), testCategory.getName());
    }

    @Test
    void testFindCategoryByIdShouldReturnCorrectResult() {
        when(this.categoryRepository.findById("testId"))
                .thenReturn(Optional.ofNullable(category));

        CategoryServiceModel testCategory = this.categoryService.findCategory("testId");

        Assertions.assertEquals(category.getId(), testCategory.getId());
        Assertions.assertEquals(category.getName(), testCategory.getName());
    }

    @Test
    void testFindCategoryByIdShouldThrowCategoryNotFoundException() {
        Exception exception = Assertions.assertThrows(CategoryNotFoundException.class, () -> {
            this.categoryService.findCategory("testId");
        });
        Assertions.assertEquals(exception.getMessage(), CATEGORY_NOT_FOUND);
    }

    @Test
    void testEditCategoryShouldReturnCorrectResult() {
        when(this.categoryRepository.saveAndFlush(Mockito.any(Category.class)))
                .thenReturn(this.modelMapper.map(categoryServiceModel, Category.class));

        when(this.categoryRepository.findById("testId"))
                .thenReturn(Optional.ofNullable(category));

        categoryService.editCategory("testId", categoryServiceModel);

        Assertions.assertEquals(category.getName(), categoryServiceModel.getName());
    }

    @Test
    void testEditCategoryShouldThrowCategoryNotFoundException() {
        Exception exception = Assertions.assertThrows(CategoryNotFoundException.class, () -> {
            this.categoryService.editCategory("testId", categoryServiceModel);
        });
        Assertions.assertEquals(exception.getMessage(), CATEGORY_NOT_FOUND);
    }

    @Test
    void testDeleteCategoryShouldWorkCorrect() {
        when(this.categoryRepository.findById("testId"))
                .thenReturn(Optional.ofNullable(category));

        this.categoryService.deleteCategory("testId");
        Assertions.assertEquals(categoryRepository.findAll().size(), 0);
    }

    @Test
    void testDeleteCategoryWithInvalidIdShouldThrowCategoryNotFoundException() {
        Exception exception = Assertions.assertThrows(CategoryNotFoundException.class, () -> {
            this.categoryService.deleteCategory("testId");
        });
        Assertions.assertEquals(exception.getMessage(), CATEGORY_NOT_FOUND);
    }

    @Test
    void testAllCategoriesShouldReturnCorrectResult() {
        when(this.categoryRepository.findAll())
                .thenReturn(List.of(category));
        List<Category> categoryList = this.categoryService.findAll();
        Category testCategory = categoryList.get(0);

        Assertions.assertEquals(1, categoryList.size());
        Assertions.assertEquals(category.getName(), testCategory.getName());
    }

}