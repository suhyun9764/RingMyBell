package com.ringmabell.whichme_backend.service;

import static com.ringmabell.whichme_backend.constants.DispatchMessage.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ringmabell.whichme_backend.dto.DispatchJoinDto;
import com.ringmabell.whichme_backend.entitiy.Role;
import com.ringmabell.whichme_backend.entitiy.dispatch.Dispatch;
import com.ringmabell.whichme_backend.entitiy.dispatch.SubUnit;
import com.ringmabell.whichme_backend.exception.exptions.DuplicateException;
import com.ringmabell.whichme_backend.repository.DispatchRepository;
import com.ringmabell.whichme_backend.repository.SubUnitRepository;
import com.ringmabell.whichme_backend.response.Response;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DispatchServiceImpl implements DispatchService {
	private final DispatchRepository dispatchRepository;
	private final SubUnitRepository subUnitRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Response joinDispatch(DispatchJoinDto dispatchJoinDto) {
		String vehicleNumber = dispatchJoinDto.getVehicleNumber();
		SubUnit subUnit = getSubUnit(dispatchJoinDto);
		if (dispatchRepository.existsByLoginId(vehicleNumber))
			throw new DuplicateException(ALREADY_EXIST_VEHICLE);

		String location = subUnit.getStation().getLocation();

		Dispatch dispatch = Dispatch.builder()
			.loginId(vehicleNumber)
			.activityArea(location)
			.subUnit(subUnit)
			.password(passwordEncoder.encode(dispatchJoinDto.getPassword()))
			.role(Role.ROLE_DISPATCH)
			.build();

		dispatchRepository.save(dispatch);

		return Response.builder()
			.success(true)
			.message(COMPLETE_VEHICLE_JOIN)
			.build();
	}

	private SubUnit getSubUnit(DispatchJoinDto dispatchJoinDto) {
		Long subUnit = dispatchJoinDto.getSubUnit();
		return subUnitRepository.findById(subUnit).orElseThrow(() ->
			new EntityNotFoundException(NOT_EXIST_SUBUNIT));
	}
}
