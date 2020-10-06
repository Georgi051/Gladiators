package project.gladiators.model.entities;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.List;

@Entity
@Table(name = "days")
public class Days extends BaseEntity{

    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @OneToMany
    private List<Workout> workouts;

}
