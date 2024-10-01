package com.ringmabell.whichme_backend.service;

import com.ringmabell.whichme_backend.dto.DispatchJoinDto;
import com.ringmabell.whichme_backend.response.Response;

public interface DispatchService {
	Response joinDispatch(DispatchJoinDto dispatchJoinDto);
}
