package com.example;

import com.example.MODEL.ERole;
import com.example.MODEL.RoleEntity;
import com.example.MODEL.UserEntity;
import com.example.REPOSITORY.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.util.Set;

@SpringBootApplication
public class SpringSeJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSeJwtApplication.class, args);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Bean
    CommandLineRunner init() {
        return args -> {
            UserEntity userEntity = UserEntity.builder()
                    .email("admin@gmail.com")
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(Set.of(RoleEntity.builder()
                            .name(ERole.ADMIN)
                            .build()))
                    .build();

            userRepository.save(userEntity);

        };
    }
    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

}
