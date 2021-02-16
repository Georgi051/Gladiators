package project.gladiators.integrational.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.gladiators.model.entities.Muscle;
import project.gladiators.repository.MuscleRepository;
import project.gladiators.service.MuscleService;
import project.gladiators.service.serviceModels.MuscleServiceModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MuscleServiceImplTest {

    Muscle muscle;
    Muscle muscle2;

    @MockBean
    MuscleRepository muscleRepository;

    @Autowired
    MuscleService muscleService;

    @BeforeEach
    public void setUp(){
        muscle = new Muscle();
        muscle.setId("1");
        muscle.setName("Test");
        muscle2 = new Muscle();
        muscle2.setId("2");
        muscle2.setName("Test2");
    }

    @Test
    public void findAll_shouldReturnCollectionOfAllMuscles(){
        when(muscleRepository.findAll())
                .thenReturn(List.of(muscle, muscle2));

        List<MuscleServiceModel> muscles = muscleService.findAll();

        assertEquals(2, muscles.size());
    }

    @Test
    public void findByName_shouldFindMuscleByName(){
        when(muscleRepository.findByName("Test"))
                .thenReturn(muscle);
        MuscleServiceModel muscleServiceModel = muscleService.findByName("Test");

        assertEquals("1", muscleServiceModel.getId());
    }

}
