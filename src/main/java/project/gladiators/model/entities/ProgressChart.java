package project.gladiators.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "progress_charts")
@Getter
@Setter
@NoArgsConstructor
public class ProgressChart extends BaseEntity{

    @Column(name = "progress_date", nullable = false)
    private LocalDate progressDate;

    @Column
    private double weight;

    @Column
    private double height;

    @Column
    private double BMI;

    @Column
    private double waist;

    @Column
    private double chest;

    @Column
    private double biceps;

    @Column
    private double thigh;

}
