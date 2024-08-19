package taco.klkl.global.common.response;

import java.time.LocalDateTime;

import taco.klkl.global.error.ErrorResponse;

public record GlobalResponse(boolean isSuccess, int status, Object data, LocalDateTime timestamp) {
	public static GlobalResponse ok(int status, Object data) {
		return new GlobalResponse(true, status, data, LocalDateTime.now());
	}

	public static GlobalResponse error(int status, ErrorResponse errorResponse) {
		return new GlobalResponse(false, status, errorResponse, LocalDateTime.now());
	}
}
