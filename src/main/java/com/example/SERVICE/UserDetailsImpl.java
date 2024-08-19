package com.example.SERVICE;

import com.example.MODEL.RoleEntity;
import com.example.MODEL.UserEntity;
import com.example.REPOSITORY.RoleRepository;
import com.example.REPOSITORY.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserDetailsImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Asegúrate de tener PasswordEncoder en el servicio

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

        Collection<? extends GrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())))
                .collect(Collectors.toSet());
        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }

    @Transactional
    public Optional<UserEntity> updateUser(Long id, UserEntity userDetails) {
        Optional<UserEntity> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            UserEntity existingUser = existingUserOptional.get();

            existingUser.setUsername(userDetails.getUsername());
            existingUser.setEmail(userDetails.getEmail());

            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }

            // Manejo de roles
            Set<RoleEntity> roles = userDetails.getRoles().stream()
                    .map(role -> {
                        // Buscar el rol por nombre en la base de datos
                        return roleRepository.findByName(role.getName())
                                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + role.getName()));
                    })
                    .collect(Collectors.toSet());

            existingUser.setRoles(roles);

            try {
                UserEntity updatedUser = userRepository.save(existingUser);
                return Optional.of(updatedUser);
            } catch (Exception e) {
                throw new RuntimeException("Error al guardar los cambios", e);
            }
        } else {
            throw new IllegalArgumentException("El usuario no existe.");
        }
    }
    public List<UserEntity> listAllUsers() {
        List<UserEntity> userList = new ArrayList<>();
        userRepository.findAll().forEach(userList::add);
        return userList;
    }

    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity save(UserEntity userToUpdate) {
        return userRepository.save(userToUpdate);
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        }

        if (username != null) {
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } else {
            throw new IllegalStateException("No user is currently authenticated");
        }
    }

}
