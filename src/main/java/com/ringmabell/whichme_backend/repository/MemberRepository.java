package com.ringmabell.whichme_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ringmabell.whichme_backend.entitiy.Member;
import com.ringmabell.whichme_backend.entitiy.user.User;

public interface MemberRepository extends JpaRepository<Member,Long> {
	Optional<Member> findByLoginId(String loginId);
}
