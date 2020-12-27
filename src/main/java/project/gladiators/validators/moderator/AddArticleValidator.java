package project.gladiators.validators.moderator;

import org.springframework.validation.Errors;
import project.gladiators.annotations.Validator;
import project.gladiators.model.bindingModels.ArticleRegisterBindingModel;

import static project.gladiators.constants.validators.ModeratorConstants.*;

@Validator
public class AddArticleValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return ArticleRegisterBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ArticleRegisterBindingModel articleRegisterBindingModel = (ArticleRegisterBindingModel) o;

        if (articleRegisterBindingModel.getTitle().length() < 8 ||
                articleRegisterBindingModel.getTitle().length() > 80){
            errors.rejectValue("title",ARTICLE_TITLE_LENGTH,ARTICLE_TITLE_LENGTH);
        }

        if (articleRegisterBindingModel.getDescription().length() < 50 ||
        articleRegisterBindingModel.getDescription().length() > 2000){
            errors.rejectValue("description",ARTICLE_DESCRIPTION_LENGTH,ARTICLE_DESCRIPTION_LENGTH);
        }

        if (articleRegisterBindingModel.getImage().getOriginalFilename().isEmpty()){
            errors.rejectValue("image",ARTICLE_IMAGE_CANNOT_BE_NULL,ARTICLE_IMAGE_CANNOT_BE_NULL);
        }

    }
}
