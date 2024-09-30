package com.ringmabell.whichme_backend.repository;

import com.ringmabell.whichme_backend.entitiy.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    RefreshToken findByUsername(String username);
}
