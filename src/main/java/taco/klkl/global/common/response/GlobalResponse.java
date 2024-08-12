package taco.klkl.global.common.response;

import java.time.LocalDateTime;

import taco.klkl.global.error.ErrorResponse;

public record GlobalResponse(boolean isSuccess, String code, Object data, LocalDateTime timestamp) {
	public static GlobalResponse ok(String code, Object data) {
		return new GlobalResponse(true, code, data, LocalDateTime.now());
	}

	public static GlobalResponse error(String code, ErrorResponse errorResponse) {
		return new GlobalResponse(false, code, errorResponse, LocalDateTime.now());
	}
}
