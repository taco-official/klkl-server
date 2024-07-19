package taco.klkl.global.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;
import taco.klkl.global.response.GlobalResponse;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex,
		HttpHeaders headers,
		HttpStatusCode statusCode,
		WebRequest request
	) {
		log.error("MethodArgumentNotValid : {}", ex.getMessage(), ex);
		final ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_INVALID;
		final ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage());
		final GlobalResponse globalResponse = GlobalResponse.error(errorCode.getCode(), errorResponse);
		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
		HttpRequestMethodNotSupportedException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		log.error("HttpRequestMethodNotSupported : {}", ex.getMessage(), ex);
		final ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
		final ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage());
		final GlobalResponse globalResponse = GlobalResponse.error(errorCode.getCode(), errorResponse);
		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
		Exception ex,
		Object body,
		HttpHeaders headers,
		HttpStatusCode statusCode,
		WebRequest request
	) {
		log.error("ExceptionInternal : {}", ex.getMessage(), ex);
		final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		final ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage());
		final GlobalResponse globalResponse = GlobalResponse.error(errorCode.getCode(), errorResponse);
		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Object> handleCustomException(CustomException ex) {
		log.error("CustomException : {}", ex.getMessage(), ex);
		final ErrorCode errorCode = ex.getErrorCode();
		final ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage());
		final GlobalResponse globalResponse = GlobalResponse.error(errorCode.getCode(), errorResponse);
		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception ex) {
		log.error("InternalServerError : {}", ex.getMessage(), ex);
		final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		final ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage());
		final GlobalResponse globalResponse = GlobalResponse.error(errorCode.getCode(), errorResponse);
		return ResponseEntity.status(errorCode.getStatus()).body(globalResponse);
	}
}
