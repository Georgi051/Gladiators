package project.gladiators.service;

import project.gladiators.service.serviceModels.RoleServiceModel;

import java.util.Set;

public interface RoleService {


    Set<RoleServiceModel> findAllRoles();

    RoleServiceModel findByAuthority(String authority);
}
