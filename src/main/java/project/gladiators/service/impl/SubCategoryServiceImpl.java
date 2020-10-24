package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.gladiators.repository.SubCategoryRepository;
import project.gladiators.service.SubCategoryService;
import project.gladiators.service.serviceModels.SubCategoryServiceModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final ModelMapper modelMapper;


    public SubCategoryServiceImpl(SubCategoryRepository subCategoryRepository, ModelMapper modelMapper) {
        this.subCategoryRepository = subCategoryRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<SubCategoryServiceModel> allSubCategories() {
        return this.subCategoryRepository.findAll().stream()
                .map(c -> this.modelMapper.map(c, SubCategoryServiceModel.class))
                .collect(Collectors.toList());
    }
}
