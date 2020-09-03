package project.gladiators.service.serviceModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.gladiators.model.entities.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ArticleServiceModel extends BaseServiceModel {

    private String title;

    private String description;


    private LocalDateTime addedOn;


    private String imageUrl;


    private UserServiceModel  userServiceModel;
}
