package project.gladiators.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.gladiators.model.entities.Role;
import project.gladiators.repository.RoleRepository;

@Component
@Order(value = 1)
public class RolesInitialization implements CommandLineRunner {
    private RoleRepository roleRepository;

    @Autowired
    public RolesInitialization(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            this.roleRepository.save(new Role("ROLE_ROOT"));
            this.roleRepository.save(new Role("ROLE_ADMIN"));
            this.roleRepository.save(new Role("ROLE_MODERATOR"));
            this.roleRepository.save(new Role("ROLE_USER"));
            this.roleRepository.save(new Role("ROLE_TRAINER_CONFIRMED"));
            this.roleRepository.save(new Role("ROLE_TRAINER_UNCONFIRMED"));
            this.roleRepository.save(new Role("ROLE_CUSTOMER"));
            this.roleRepository.save(new Role("ROLE_BANNED"));
        }
    }
}
