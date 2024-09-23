package taco.klkl.global.util;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import taco.klkl.global.common.response.GlobalResponse;
import taco.klkl.global.error.ErrorResponse;
import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class ResponseUtil {

	private final ObjectMapper objectMapper;

	public void sendErrorResponse(
		final HttpServletResponse response,
		final CustomException ex
	) throws IOException {
		ErrorCode errorCode = ex.getErrorCode();
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(errorCode.getHttpStatus().value());
		ErrorResponse errorResponse = ErrorResponse.of(ex.getClass().getSimpleName(), errorCode.getMessage());
		GlobalResponse globalResponse = GlobalResponse.error(errorCode.getHttpStatus().value(), errorResponse);
		response.getWriter().write(objectMapper.writeValueAsString(globalResponse));
	}
}
