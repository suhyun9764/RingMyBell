package com.ringmabell.whichme_backend.service;

import org.springframework.stereotype.Service;

import com.ringmabell.whichme_backend.entitiy.dispatch.Dispatch;
import com.ringmabell.whichme_backend.dto.DispatchJoinDto;
import com.ringmabell.whichme_backend.entitiy.dispatch.SubUnit;
import com.ringmabell.whichme_backend.exception.exptions.DuplicateException;
import com.ringmabell.whichme_backend.repository.DispatchRepository;
import com.ringmabell.whichme_backend.repository.SubUnitRepository;
import com.ringmabell.whichme_backend.response.Response;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DispatchServiceImpl implements DispatchService{
	private final DispatchRepository dispatchRepository;
	private final SubUnitRepository subUnitRepository;
	@Override
	public Response joinDispatch(DispatchJoinDto dispatchJoinDto) {
		String vehicleNumber = dispatchJoinDto.getVehicleNumber();
		SubUnit subUnit = getSubUnit(dispatchJoinDto);
		if(dispatchRepository.existsByVehicleNumber(vehicleNumber))
			throw new DuplicateException("이미 등록된 차량입니다");

		String location = subUnit.getStation().getLocation();


		Dispatch dispatch = Dispatch.builder()
			.vehicleNumber(vehicleNumber)
			.activityArea(location)
			.subUnit(subUnit)
			.build();

		dispatchRepository.save(dispatch);

		return null;
	}

	private SubUnit getSubUnit(DispatchJoinDto dispatchJoinDto) {
		Long subUnit = dispatchJoinDto.getSubUnit();
		return subUnitRepository.findById(subUnit).orElseThrow(()->
			new EntityNotFoundException("없는 소속입니다"));
	}
}
