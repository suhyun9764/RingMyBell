package com.ringmabell.whichme_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ringmabell.whichme_backend.entitiy.dispatch.Dispatch;

public interface DispatchRepository extends JpaRepository<Dispatch,Long> {
	boolean existsByVehicleNumber(String vehicleNumber);
}
