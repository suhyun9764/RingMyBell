package com.ringmabell.whichme_backend.entitiy.dispatch;

import com.ringmabell.whichme_backend.entitiy.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
public class Dispatch extends Member {

	@ManyToOne
	@JoinColumn(name = "subUnit_id", nullable = false)
	private SubUnit subUnit;
	@Column(nullable = false)
	private String activityArea;

}
