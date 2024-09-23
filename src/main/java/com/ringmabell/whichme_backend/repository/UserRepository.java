package com.ringmabell.whichme_backend.repository;

import com.ringmabell.whichme_backend.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
