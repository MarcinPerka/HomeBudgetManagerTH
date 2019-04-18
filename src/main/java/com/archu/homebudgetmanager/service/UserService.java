package com.archu.homebudgetmanager.service;


import com.archu.homebudgetmanager.exception.UserAlreadyExistAuthenticationException;
import com.archu.homebudgetmanager.model.Role;
import com.archu.homebudgetmanager.model.User;
import com.archu.homebudgetmanager.repository.RoleRepository;
import com.archu.homebudgetmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("#id == authentication.principal.id OR hasRole('ADMIN')")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(User user) throws UserAlreadyExistAuthenticationException {
        if (userRepository.findByUsername(user.getUsername()) != null || userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistAuthenticationException("User with this username or email already exists.");
        }
        Set<Role> roles = new HashSet<Role>();
        roles.add(roleRepository.findByCode("ROLE_USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);
    }

    @PreAuthorize("#id == authentication.principal.id OR hasRole('ADMIN')")
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @PreAuthorize("#id == authentication.principal.id OR hasRole('ADMIN')")
    public void updateUser(User user, Long id) throws Exception {
        User userToUpdate = userRepository.findById(id).orElse(null);

        if (userToUpdate == null)
            throw new Exception("User doesn't exists.");

        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setUsername(user.getUsername());
        userRepository.save(userToUpdate);
    }
}
