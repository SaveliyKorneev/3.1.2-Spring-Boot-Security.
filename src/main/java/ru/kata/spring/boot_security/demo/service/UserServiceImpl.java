package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RoleRepository roleRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return user;
    }


    @Override
    @Transactional
    public List<User> index() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User show(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public void save(User user,Set<Long> roleIds) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleService.getRolesByIds(roleIds));
        userRepository.save(user);
    }


    @Override
    @Transactional
    public void update(Long id, User updatedUser, Set<Long> roleIds) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        existingUser.setRoles(roleService.getRolesByIds(roleIds));

        userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


}
