package com.example.CONTROLLER;

import com.example.MODEL.RoleEntity;
import com.example.MODEL.UserEntity;
import com.example.REPOSITORY.RoleRepository;
import com.example.SERVICE.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class CrudAdminController {

    @Autowired
    private UserDetailsImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;  // Inyectar RoleRepository

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> listAllUsers() {
        try {
            List<UserEntity> users = userService.listAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al listar los usuarios.");
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserEntity userDetails) {
        Optional<UserEntity> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            UserEntity userToUpdate = userOptional.get();
            userToUpdate.setUsername(userDetails.getUsername());
            userToUpdate.setEmail(userDetails.getEmail());

            // Convertir roles de ERole a RoleEntity
            Set<RoleEntity> roles = userDetails.getRoles().stream()
                    .map(role -> roleRepository.findByName(role.getName())
                            .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + role.getName())))
                    .collect(Collectors.toSet());

            userToUpdate.setRoles(roles);

            UserEntity updatedUser = userService.save(userToUpdate);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}