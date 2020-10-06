package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class TrainingPlanWorkoutInfo extends BaseEntity{


     @OneToOne
     Workout workout;


     @Column
     @Enumerated(EnumType.STRING)
     DayOfWeek dayOfWeek;
}
