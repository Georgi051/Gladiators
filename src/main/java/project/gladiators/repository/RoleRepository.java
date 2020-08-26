package project.gladiators.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.gladiators.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    Role findByAuthority(String authority);
}