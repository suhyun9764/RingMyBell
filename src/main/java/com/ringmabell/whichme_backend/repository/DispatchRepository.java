package com.ringmabell.whichme_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ringmabell.whichme_backend.entitiy.dispatch.Dispatch;

public interface DispatchRepository extends JpaRepository<Dispatch, Long> {
	boolean existsByLoginId(String vehicleNumber);

	Optional<Dispatch> findByLoginId(String vehicleNumber);
}
