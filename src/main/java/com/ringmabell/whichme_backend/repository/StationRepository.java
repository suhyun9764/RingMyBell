package com.ringmabell.whichme_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ringmabell.whichme_backend.entitiy.dispatch.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
}