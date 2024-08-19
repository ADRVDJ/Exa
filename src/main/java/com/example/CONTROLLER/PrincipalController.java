package com.example.CONTROLLER;

import com.example.MODEL.ERole;
import com.example.MODEL.RoleEntity;
import com.example.MODEL.UserEntity;
import com.example.REPOSITORY.UserRepository;
import com.example.request.CreateUserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class PrincipalController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        ///Crear normal roles
      /*  Set<RoleEntity> roles = createUserDTO.getRoles().stream()
                .map(role -> RoleEntity.builder()
                        .name(ERole.valueOf(role))
                        .build())
                .collect(Collectors.toSet());*/
        // Crear un conjunto de roles con solo el rol USER
        Set<RoleEntity> roles = Set.of(
                RoleEntity.builder()
                        .name(ERole.INVITED)  // Asignar siempre el rol USER
                        .build()
        );

        UserEntity userEntity = UserEntity.builder()
                .username(createUserDTO.getUsername())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))  // Aquí se asigna el password
                .email(createUserDTO.getEmail())        // Aquí se asigna el email
                .roles(roles)
                .build();

        userRepository.save(userEntity);

        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam String id) {
        userRepository.deleteById(Long.parseLong(id));
        return ResponseEntity.ok("Se ha borrado el usuario con id: " + id);
    }
}
