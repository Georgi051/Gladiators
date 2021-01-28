package project.gladiators.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import project.gladiators.constants.RoleConstants;
import project.gladiators.model.entities.*;
import project.gladiators.model.enums.Gender;
import project.gladiators.repository.*;

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
    private CustomerRepository customerRepository;
    private ProgressChartRepository progressChartRepository;

    @Autowired
    public UsersInitialization(UserRepository userRepository, TrainerRepository trainerRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, CustomerRepository customerRepository, ProgressChartRepository progressChartRepository) {
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.progressChartRepository = progressChartRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        if (userRepository.count() == 0) {
//
//            Role roleRoot = this.roleRepository.findByAuthority(("ROLE_ROOT"));
//            Role roleAdmin = this.roleRepository.findByAuthority(("ROLE_ADMIN"));
//            Role roleModerator = this.roleRepository.findByAuthority(("ROLE_MODERATOR"));
//            Role roleUser = this.roleRepository.findByAuthority(("ROLE_USER"));
//            Role roleTrainerUnconfirmed = this.roleRepository.findByAuthority("ROLE_TRAINER_UNCONFIRMED");
//            Role roleTrainerConfirmed = this.roleRepository.findByAuthority("ROLE_TRAINER_CONFIRMED");
//            Role roleCustomer = this.roleRepository.findByAuthority(RoleConstants.CUSTOMER);
//
//            User root = new User("root", "rootFirstName", "rootLastName", LocalDateTime.now(), passwordEncoder.encode("1234")
//                    , "root1234@abg.bg", "https://res.cloudinary.com/gladiators/image/upload/v1599468350/profile-pictures/profile_quok32_qsbjk2.jpg",
//                    Gender.MALE, LocalDate.of(2000, 9, 24),
//                    Set.of(roleAdmin, roleRoot, roleModerator, roleUser), true);
//
//            User user = new User("user", null, null, LocalDateTime.now(), passwordEncoder.encode("1234")
//                    , "user@abv.bg", null, null, null, Set.of(roleUser), true);
//
//            User trainerUnconfirmed = new User("trainer_unconfirmed",
//                    null, null, LocalDateTime.now()
//                    , passwordEncoder.encode("1234"), "trainer_unc@abv.bg", null
//                    , null, null,Set.of(roleUser, roleTrainerUnconfirmed), true);
//
//            User trainerConfirmed = new User("trainer_confirmed",
//                    "trainerConfirmedFirstName", "trainerConfirmedLastName", LocalDateTime.now()
//                    , passwordEncoder.encode("1234"), "trainer_conf@abv.bg", "https://res.cloudinary.com/gladiators/image/upload/v1600681759/profile-pictures/photo-1507398941214-572c25f4b1dc_dgdoa2.jpg"
//                    , Gender.MALE, LocalDate.of(1923, 1, 15), Set.of(roleUser, roleTrainerConfirmed), true);
//
//            Trainer trainerUnconfirmedT = new Trainer(2, "Test description Test description Test description Test description Test description Test description",
//                    new HashSet<>(), new ArrayList<>(), trainerUnconfirmed);
//
//
//            Trainer trainerConfirmedT = new Trainer(10, "Test description Test description Test description Test description Test description Test description",
//                    new HashSet<>(), new ArrayList<>(), trainerConfirmed);
//
//            User customer = new User("customer",
//                    "Customer", "Customer", LocalDateTime.now()
//                    , passwordEncoder.encode("1234"), "customer@abv.bg", "https://res.cloudinary.com/gladiators/image/upload/v1600681759/profile-pictures/photo-1507398941214-572c25f4b1dc_dgdoa2.jpg"
//                    , Gender.MALE, LocalDate.of(1923, 1, 15), Set.of(roleUser, roleCustomer), true);
//
//            Customer customer1 = new Customer();
//            customer1.setUser(customer);
//            customer1.setHeight(175);
//            customer1.setWeight(80);
//            customer1.setBMI(25);
//            customer1.setProgressChart(new ProgressChart());
//            customer1.getProgressChart().setBMI(25);
//            customer1.getProgressChart().setHeight(175);
//            customer1.getProgressChart().setWeight(80);
//            customer1.getProgressChart().setChest(80);
//            customer1.getProgressChart().setBiceps(45);
//            customer1.getProgressChart().setThigh(80);
//            customer1.getProgressChart().setWaist(90);
//            customer1.getProgressChart().setProgressDate(LocalDate.now());
//            customer1.getProgressChart().setChanged(true);
//            userRepository.saveAll(List.of(root, user, trainerConfirmed, trainerUnconfirmed, customer));
//            customerRepository.saveAndFlush(customer1);
//            trainerRepository.saveAll(List.of(trainerConfirmedT, trainerUnconfirmedT));
//        }
    }
}
