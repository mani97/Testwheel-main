package com.aishu.spring_security.Repository;

import com.aishu.spring_security.model.User;
import com.aishu.spring_security.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface TokenRepository extends JpaRepository<VerificationToken, Long> {


    Optional<VerificationToken> findByToken(String tokenValue);


    // Query by User entity
    Optional<VerificationToken> findByUser(User user);

    // Query by User.username (Spring Data will navigate into the relation)
    Optional<VerificationToken> findByUserUsername(String username);
}
