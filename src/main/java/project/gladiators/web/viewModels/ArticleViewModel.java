package project.gladiators.web.viewModels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ArticleViewModel  extends BaseViewModel{

    private String title;

    private String description;


    private LocalDateTime addedOn;

    private String imageUrl;

    private String userUsername;
}
