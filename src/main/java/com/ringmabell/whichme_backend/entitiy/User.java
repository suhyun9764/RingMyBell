package com.ringmabell.whichme_backend.entitiy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String realName;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String phone;
	@Column(nullable = false)
	private String address;
	@Column(nullable = false)
	private String provider;
	private String providerId;

}
