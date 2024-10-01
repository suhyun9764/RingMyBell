package com.ringmabell.whichme_backend.repository;

import java.util.Optional;

import com.ringmabell.whichme_backend.entitiy.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

	Optional<User> findByUsername(String username);
}
