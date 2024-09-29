package com.ringmabell.whichme_backend.repository;

import org.springframework.data.repository.CrudRepository;

import com.ringmabell.whichme_backend.entitiy.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

	void deleteByRefreshToken(String refreshToken);
	RefreshToken findByUsername(String username);
}
