package com.maverickbank.service;

import com.maverickbank.entity.Role;
import com.maverickbank.entity.User;
import com.maverickbank.repository.RoleRepository;
import com.maverickbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public User registerCustomer(User user) {
        // Calculate age from date of birth
        if (user.getDateOfBirth() != null) {
            user.setAge(Period.between(user.getDateOfBirth(), java.time.LocalDate.now()).getYears());
        }
        
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role customerRole = roleRepository.findByName("CUSTOMER").orElseThrow();
        user.setRoles(Collections.singleton(customerRole));
        user.setIsActive(true);
        return userRepository.save(user);
    }

    public User registerEmployee(User user) {
        // Calculate age from date of birth
        if (user.getDateOfBirth() != null) {
            user.setAge(Period.between(user.getDateOfBirth(), java.time.LocalDate.now()).getYears());
        }
        
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role employeeRole = roleRepository.findByName("EMPLOYEE").orElseThrow();
        user.setRoles(Collections.singleton(employeeRole));
        user.setIsActive(true);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }

    public List<User> getUsersByRole(String roleName) {
        return userRepository.findActiveUsersByRoleName(roleName);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUserProfile(Long id, User updated) {
        User user = getUserById(id);
        user.setName(updated.getName());
        user.setEmail(updated.getEmail());
        user.setContactNumber(updated.getContactNumber());
        user.setAddress(updated.getAddress());
        user.setDateOfBirth(updated.getDateOfBirth());
        user.setAadharNumber(updated.getAadharNumber());
        user.setPanNumber(updated.getPanNumber());
        user.setGender(updated.getGender());
        
        // Recalculate age if date of birth changed
        if (user.getDateOfBirth() != null) {
            user.setAge(Period.between(user.getDateOfBirth(), java.time.LocalDate.now()).getYears());
        }
        
        return userRepository.save(user);
    }

    public User changePassword(Long id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public User updateLastLogin(Long id) {
        User user = getUserById(id);
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void deactivateUser(Long id) {
        User user = getUserById(id);
        user.setIsActive(false);
        userRepository.save(user);
    }

    public void activateUser(Long id) {
        User user = getUserById(id);
        user.setIsActive(true);
        userRepository.save(user);
    }

    public User assignRole(Long userId, String roleName) {
        User user = getUserById(userId);
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Role> roles = user.getRoles();
        if (roles == null) roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User removeRole(Long userId, String roleName) {
        User user = getUserById(userId);
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Role> roles = user.getRoles();
        if (roles != null) {
            roles.remove(role);
            user.setRoles(roles);
        }
        return userRepository.save(user);
    }
}
