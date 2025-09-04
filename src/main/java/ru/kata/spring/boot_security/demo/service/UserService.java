package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> index();

    User show(Long id);

    void save(User user,Set<Long> role);

    void update(Long id, User updatedUser, Set<Long> roleIds);

    void delete(Long id);

    User findByUsername(String username);

}
