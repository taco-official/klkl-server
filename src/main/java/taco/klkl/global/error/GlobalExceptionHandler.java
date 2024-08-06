package taco.klkl.global.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.global.common.response.GlobalResponse;
import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private static final String ERROR_MESSAGE_DELIMITER = " ";

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex,
		HttpHeaders headers,
		HttpStatusCode statusCode,
		WebRequest request
	) {
		log.error("MethodArgumentNotValid : {}", ex.getMessage(), ex);

		// 모든 오류 메시지를 수집
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
			String fieldName = fieldError.getField();
			String errorMessage = fieldError.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		final ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_INVALID;
		final String errorMessage = String.join(ERROR_MESSAGE_DELIMITER, errorCode.getMessage(), errors.toString());
		final ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), errorMessage);
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
		final ErrorCode errorCode = ErrorCode.METHOD_NOT_SUPPORTED;
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

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
		HttpMessageNotReadableException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		log.error("HttpMessageNotReadable : {}", ex.getMessage(), ex);
		final ErrorCode errorCode = ErrorCode.HTTP_MESSAGE_NOT_READABLE;
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

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		log.error("MethodArgumentTypeMismatchException : {}", ex.getMessage(), ex);
		final ErrorCode errorCode = ErrorCode.QUERY_TYPE_MISMATCH;
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
