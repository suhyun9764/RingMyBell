package com.ringmabell.whichme_backend.entitiy.dispatch;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "station")
@Getter
@Setter
public class Station {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;  // 소방서/경찰서 이름
	private String type;  // "소방서" 또는 "경찰서"
	private String location;  // 위치
	@OneToMany(mappedBy = "station", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SubUnit> subUnits;

}