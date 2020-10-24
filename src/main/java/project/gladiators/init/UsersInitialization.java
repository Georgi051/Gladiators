package project.gladiators.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import project.gladiators.model.entities.Role;
import project.gladiators.model.entities.Trainer;
import project.gladiators.model.entities.User;
import project.gladiators.model.enums.Gender;
import project.gladiators.repository.RoleRepository;
import project.gladiators.repository.TrainerRepository;
import project.gladiators.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Order(value = 2)
public class UsersInitialization implements CommandLineRunner {
    private UserRepository userRepository;
    private TrainerRepository trainerRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public UsersInitialization(UserRepository userRepository, TrainerRepository trainerRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {

            Role roleRoot = this.roleRepository.findByAuthority(("ROLE_ROOT"));
            Role roleAdmin = this.roleRepository.findByAuthority(("ROLE_ADMIN"));
            Role roleModerator = this.roleRepository.findByAuthority(("ROLE_MODERATOR"));
            Role roleUser = this.roleRepository.findByAuthority(("ROLE_USER"));
            Role roleTrainerUnconfirmed = this.roleRepository.findByAuthority("ROLE_TRAINER_UNCONFIRMED");
            Role roleTrainerConfirmed = this.roleRepository.findByAuthority("ROLE_TRAINER_CONFIRMED");

            User root = new User("root", "rootFirstName", "rootLastName", LocalDateTime.now(), passwordEncoder.encode("1234")
                    , "root1234@abg.bg", "https://res.cloudinary.com/gladiators/image/upload/v1599468350/profile-pictures/profile_quok32_qsbjk2.jpg",
                    Gender.MALE, LocalDate.of(2000, 9, 24),
                    Set.of(roleAdmin, roleRoot, roleModerator, roleUser));

            User user = new User("user", null, null, LocalDateTime.now(), passwordEncoder.encode("1234")
                    , "user@abv.bg", null, null, null, Set.of(roleUser));

            User trainerUnconfirmed = new User("trainer_unconfirmed",
                    null, null, LocalDateTime.now()
                    , passwordEncoder.encode("1234"), "trainer_unc@abv.bg", null
                    , null, null,Set.of(roleUser, roleTrainerUnconfirmed));

            User trainerConfirmed = new User("trainer_confirmed",
                    "trainerConfirmedFirstName", "trainerConfirmedLastName", LocalDateTime.now()
                    , passwordEncoder.encode("1234"), "trainer_conf@abv.bg", "https://res.cloudinary.com/gladiators/image/upload/v1600681759/profile-pictures/photo-1507398941214-572c25f4b1dc_dgdoa2.jpg"
                    , Gender.MALE, LocalDate.of(1923, 1, 15), Set.of(roleUser, roleTrainerConfirmed));

            Trainer trainerUnconfirmedT = new Trainer(2, "Test description Test description Test description Test description Test description Test description",
                    new HashSet<>(), new ArrayList<>(), trainerUnconfirmed);


            Trainer trainerConfirmedT = new Trainer(10, "Test description Test description Test description Test description Test description Test description",
                    new HashSet<>(), new ArrayList<>(), trainerConfirmed);

            userRepository.saveAll(List.of(root, user, trainerConfirmed, trainerUnconfirmed));
            trainerRepository.saveAll(List.of(trainerConfirmedT, trainerUnconfirmedT));
        }
    }
}
