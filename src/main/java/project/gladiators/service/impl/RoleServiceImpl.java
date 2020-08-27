package project.gladiators.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gladiators.model.entities.Role;
import project.gladiators.repository.RoleRepository;
import project.gladiators.service.RoleService;
import project.gladiators.service.serviceModels.RoleServiceModel;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedRoleInDb() {
        this.roleRepository.saveAndFlush(new Role("ROLE_ROOT"));
        this.roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
        this.roleRepository.saveAndFlush(new Role("ROLE_MODERATOR"));
        this.roleRepository.saveAndFlush(new Role("ROLE_USER"));
        this.roleRepository.saveAndFlush(new Role("ROLE_TRAINER"));
        this.roleRepository.saveAndFlush(new Role("ROLE_CUSTOMER"));
    }

    @Override
    public Set<RoleServiceModel> findAllRoles() {
        return this.roleRepository.findAll().stream()
                .map(r -> this.modelMapper.map(r, RoleServiceModel.class))
                .collect(Collectors.toSet());
    }

    @Override
    public RoleServiceModel findByAuthority(String authority) {
        return this.modelMapper.map(this.roleRepository.findByAuthority(authority), RoleServiceModel.class);
    }
}
