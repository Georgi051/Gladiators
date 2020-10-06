package project.gladiators.web.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DaysOfWeekRestController {

    public List<DayOfWeek> getDaysOfWeek(){
        return List.of(DayOfWeek.values());
    }
}
