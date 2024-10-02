package com.ringmabell.whichme_backend.entitiy.dispatch;

import com.ringmabell.whichme_backend.entitiy.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Dispatch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String vehicleNumber;
	@Column(nullable = false)
	private String password;
	@ManyToOne
	@JoinColumn(name = "subUnit_id",nullable = false)
	private SubUnit subUnit;
	@Column(nullable = false)
	private String activityArea;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
}
