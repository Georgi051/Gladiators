package project.gladiators.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.gladiators.exceptions.SubCategoryNotFoundException;
import project.gladiators.model.entities.Product;
import project.gladiators.model.entities.SubCategory;
import project.gladiators.repository.SubCategoryRepository;
import project.gladiators.service.SubCategoryService;
import project.gladiators.service.serviceModels.ProductServiceModel;
import project.gladiators.service.serviceModels.SubCategoryServiceModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.gladiators.constants.ExceptionMessages.SUBCATEGORY_NOT_FOUND;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SubCategoryServiceImplTest {
    private final String PRODUCT_NAME = "Syntha-6";
    private final String PRODUCT_MANUFACTURER_NAME = "BSN";

    @Autowired
    SubCategoryService subCategoryService;

    @MockBean
    SubCategoryRepository subCategoryRepository;

    @Autowired
    ModelMapper modelMapper;

    private SubCategory subCategory;
    private SubCategoryServiceModel subCategoryServiceModel;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product = new Product();
        product.setId("1");
        product.setName(PRODUCT_NAME);
        product.setManufacturerName(PRODUCT_MANUFACTURER_NAME);

        subCategory = new SubCategory();
        subCategory.setId("1");
        subCategory.setName("bcaa");
        subCategory.setEmpty(true);
        subCategory.setProducts(Set.of(product));

        subCategoryServiceModel = new SubCategoryServiceModel();
        subCategoryServiceModel.setName("BCCA new formula");
    }

    @Test
    void testSeedSubCategoryShouldSaveSubCategoryCorrect() {
        SubCategoryServiceModel subCategoryServiceModel =
                this.modelMapper.map(subCategory, SubCategoryServiceModel.class);
        this.subCategoryService.seedSubCategory(subCategoryServiceModel);

        verify(subCategoryRepository).saveAndFlush(any());
    }

    @Test
    void editSubCategoryShouldReturnCorrectResult() {
        when(this.subCategoryRepository.save(any(SubCategory.class)))
                .thenReturn(subCategory);

        when(this.subCategoryRepository.findById("1"))
                .thenReturn(Optional.ofNullable(subCategory));

        this.subCategoryService.editSubCategory("1", subCategoryServiceModel);

        Assertions.assertEquals(subCategory.getName(), subCategoryServiceModel.getName());
    }

    @Test
    void editSubCategoryShouldThrowSubCategoryNotFoundException() {
        Exception exception = Assertions.assertThrows(SubCategoryNotFoundException.class, () -> {
            this.subCategoryService.editSubCategory("testId", subCategoryServiceModel);
        });
        Assertions.assertEquals(exception.getMessage(), SUBCATEGORY_NOT_FOUND);
    }

    @Test
    void testAllSubCategoriesShouldReturnCorrectResult() {
        when(this.subCategoryRepository.findAll())
                .thenReturn(List.of(subCategory));

        List<SubCategoryServiceModel> allSubCategories = this.subCategoryService.allSubCategories();
        SubCategoryServiceModel testSubCategory = allSubCategories.get(0);

        Assertions.assertEquals(1, allSubCategories.size());
        Assertions.assertEquals(subCategory.getName(), testSubCategory.getName());
    }

    @Test
    void testFindByIdShouldReturnCorrectResult() {
        when(this.subCategoryRepository.findById("1"))
                .thenReturn(Optional.ofNullable(subCategory));

        SubCategoryServiceModel testSubCategory = this.subCategoryService.findById("1");

        Assertions.assertEquals(subCategory.getId(), testSubCategory.getId());
        Assertions.assertEquals(subCategory.getName(), testSubCategory.getName());
    }

    @Test
    void testFindByIdShouldThrowSubCategoryNotFoundException() {
        Exception exception = Assertions.assertThrows(SubCategoryNotFoundException.class, () -> {
            this.subCategoryService.findById("testId");
        });
        Assertions.assertEquals(exception.getMessage(), SUBCATEGORY_NOT_FOUND);
    }
}