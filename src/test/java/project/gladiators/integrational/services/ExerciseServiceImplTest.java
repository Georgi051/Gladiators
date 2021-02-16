package project.gladiators.integrational.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.model.entities.Exercise;
import project.gladiators.repository.ExerciseRepository;
import project.gladiators.service.ExerciseService;
import project.gladiators.service.serviceModels.ExerciseServiceModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ExerciseServiceImplTest {

    Exercise exercise;
    Exercise exercise1;
    List<Exercise> exercises;

    @MockBean
    ExerciseRepository exerciseRepository;

    @Autowired
    ExerciseService exerciseService;

    @Autowired
    ModelMapper modelMapper;

    @BeforeEach
    public void setup(){
        exercise = new Exercise();
        exercise.setId("1");
        exercise1 = new Exercise();
        exercise1.setId("2");
        exercises = new ArrayList<>();
        exercises.addAll(List.of(exercise, exercise1));
    }

    @Test
    public void findAll_shouldReturnCollectionOfAllExercises(){

        when(exerciseRepository.findAll())
                .thenReturn(exercises);
        List<ExerciseServiceModel> exerciseServiceModels = exerciseService.findAll();

        assertEquals(exercises.size(), exerciseServiceModels.size());
    }

    @Test
    public void addExercise_shouldAddExerciseInRepo() throws IOException {

        MultipartFile multipartFile = new MockMultipartFile("picture",
                "https://res.cloudinary.com/gladiators/image/upload/v1599061356/No-image-found_vtfx1x.jpg",
                IMAGE_JPEG.getMimeType(), (byte[]) null);
        ExerciseServiceModel exerciseServiceModel = modelMapper
                .map(exercise, ExerciseServiceModel.class);

        exerciseService.addExercise(exerciseServiceModel, multipartFile);

        verify(exerciseRepository).saveAndFlush(any(Exercise.class));
    }


}
