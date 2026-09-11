package com.aishu.spring_security.Repository;

import com.aishu.spring_security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByPhone(String phone);

    Optional<User> findByUsername(String username);

    boolean existsByPhone(String phone);


    boolean existsByUsername(String username);

}