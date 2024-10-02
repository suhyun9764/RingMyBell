package com.ringmabell.whichme_backend.constants;

public class DispatchSwaggerMessages {
	public static final String COMPLETE_VEHICLE_JOIN_EXAMPLE =  "{ \"success\": true, \"message\": \"차량등록이 완료되었습니다\" }";
	public static final String VEHICLE_DUPLICATE_EXAMPLE = "{ \"success\": false,  \"httpStatus\": \"CONFLICT\", \"code\": 409, \"message\": \"이미 등록된 차량입니다\" }";
	public static final String VEHICLE_ERROR_EXAMPLE = "{ \"success\": false,  \"httpStatus\": \"BAD_REQUEST\", \"code\": 400, \"message\": \"차량 번호는 998 또는 999로 시작하고, 받침이 없는 한글 한 글자, 마지막 4자리가 숫자로 구성되어야 합니다\" }";
	public static final String SUBUNIT_ERROR_EXAMPLE = "{ \"success\": false,  \"httpStatus\": \"NOT_FOUND\", \"code\": 404, \"message\": \"존재하지 않는 부서입니다\" }";
}
