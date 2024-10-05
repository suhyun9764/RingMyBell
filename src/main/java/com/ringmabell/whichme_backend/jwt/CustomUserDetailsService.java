package com.ringmabell.whichme_backend.jwt;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ringmabell.whichme_backend.entitiy.Member;
import com.ringmabell.whichme_backend.entitiy.dispatch.Dispatch;
import com.ringmabell.whichme_backend.repository.DispatchRepository;
import com.ringmabell.whichme_backend.repository.MemberRepository;
import com.ringmabell.whichme_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;
	private final DispatchRepository dispatchRepository;
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		Optional<Member> userData = memberRepository.findByLoginId(loginId);

		if (userData.isPresent())
			return new CustomUserDetails(userData.get());

		return null;
	}

	public UserDetails loadDispatchByUsername(String vehicleNumber) throws UsernameNotFoundException {
		Optional<Dispatch> dispatchData = dispatchRepository.findByLoginId(vehicleNumber);
		if (dispatchData.isPresent())
			return new CustomUserDetails(dispatchData.get());

		return null;
	}
}
