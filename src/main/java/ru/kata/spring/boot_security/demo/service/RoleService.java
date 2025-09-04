package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Set;

@Service
public interface RoleService {

    Set<Role> getRolesByIds(Set<Long> ids);

    Set<Role> getAllRoles();
}
