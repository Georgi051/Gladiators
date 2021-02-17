package project.gladiators.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.gladiators.model.bindingModels.ArticleRegisterBindingModel;
import project.gladiators.model.bindingModels.UserRegisterBindingModel;
import project.gladiators.model.entities.Article;
import project.gladiators.model.entities.Role;
import project.gladiators.model.entities.User;
import project.gladiators.repository.ArticleRepository;
import project.gladiators.repository.RoleRepository;
import project.gladiators.repository.UserRepository;
import project.gladiators.service.UserService;
import project.gladiators.service.serviceModels.RoleServiceModel;
import project.gladiators.service.serviceModels.UserServiceModel;
import project.gladiators.web.viewModels.ArticleViewModel;

import javax.persistence.NamedQuery;
import java.security.Principal;
import java.util.*;

import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(value = "testUser", authorities = {"MODERATOR"})
class ArticleControllerTest {

    @Autowired
    ArticleController articleController;

    @MockBean
    ArticleRepository articleRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    List<Article> articles;
    Article article;
    User user;
    MockMultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        articles = new ArrayList<>();
        article = new Article();
        article.setDescription("Test");
        article.setTitle("Test");
        article.setId("1");

        user = new User();
        user.setId("1");
        article.setUser(user);

        Role roleModerator = new Role();
        roleModerator.setAuthority("MODERATOR");

        roleRepository.save(roleModerator);

        user.setAuthorities(new HashSet<>());
        user.getAuthorities().add(roleModerator);
        multipartFile = new MockMultipartFile("picture",
                "https://res.cloudinary.com/gladiators/image/upload/v1599061356/No-image-found_vtfx1x.jpg",
                IMAGE_JPEG.getMimeType(), (byte[]) null);
        userRepository.save(user);

    }

    @Test
    void addArticle_ifThereAreErrorsShouldReturnToSamePage() throws Exception {
        ArticleRegisterBindingModel article = Mockito.mock(ArticleRegisterBindingModel.class);
        article.setDescription("TestTest");
        article.setTitle("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest");
        article.setImage(multipartFile);
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("me");
        multipartFile = new MockMultipartFile("picture",
                "https://res.cloudinary.com/gladiators/image/upload/v1599061356/No-image-found_vtfx1x.jpg",
                IMAGE_JPEG.getMimeType(), IMAGE_JPEG.getMimeType().getBytes());


        this.mockMvc.perform(multipart("/articles/article-add")
                .file("image", multipartFile.getBytes())
                .param("image", multipartFile.getOriginalFilename())
                .param("title", "TEST TEST")
                .param("description", "TEST TEST TEST TEST TEST TESTTEST TEST TESTTEST TEST TESTTEST TEST TESTTEST TEST TESTTEST TEST TEST")
                .content(BindingResult.MODEL_KEY_PREFIX)
                .principal(mockPrincipal)
                .content(BindingResult.MODEL_KEY_PREFIX)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(mvcResult -> mvcResult.getModelAndView().getViewName().equals("/article/article-add"));
    }

    @Test
    void addArticle_whenGetShouldReturnStatusFound() throws Exception {

        mockMvc.perform(get("/articles/article-add"))
                .andExpect(mockMvc -> {

                    ModelAndViewAssert.assertViewName(mockMvc.getModelAndView(), "article/add-article");
                });

    }

    @Test
    void getAllArticles() {
        articles.addAll(List.of(article));

        when(articleRepository.findAll())
                .thenReturn(articles);

        ModelAndView result = articleController.getAllArticles();

        List<ArticleViewModel> articleViewModels = (List<ArticleViewModel>) result.getModel().get("articles");

        assertEquals(articles.size(), articleViewModels.size());
    }

    @Test
    void getSpecificArticle_shouldReturnCorrectArticleById() {
        when(articleRepository.findById("1"))
                .thenReturn(Optional.of(article));

        ModelAndView result = articleController.getSpecificArticle("1");

        ArticleViewModel articleViewModel = (ArticleViewModel) result.getModel().get("article");

        assertEquals(article.getTitle(), articleViewModel.getTitle());
    }

    @Test
    void deleteSpecificArticle_shouldDeleteArticleAndReturnCorrectStatus() throws Exception {

        articleRepository.save(article);

        this.mockMvc.perform(get("/articles/delete/{id}", 1))
                .andExpect(status().isFound());

        assertEquals(0, articleRepository.findAll().size());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}