package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.repository.ArticleRepository;
import project.gladiators.service.ArticleService;
import project.gladiators.service.serviceModels.ArticleServiceModel;

import java.util.Arrays;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private ArticleRepository articleRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ArticleServiceModel> getAllArticles() {
        return Arrays.asList(this.modelMapper.map(articleRepository.findAll(),ArticleServiceModel[].class));
    }
}
