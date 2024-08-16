package taco.klkl.global.error;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import taco.klkl.global.common.response.GlobalResponse;
import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

class GlobalExceptionHandlerTest {

	private GlobalExceptionHandler globalExceptionHandler;

	@BeforeEach
	void setUp() {
		globalExceptionHandler = new GlobalExceptionHandler();
	}

	@Test
	@DisplayName("MethodArgumentNotValidException이 발생한 경우")
	void methodArgumentNotValidOccurred() {
		// given
		BindingResult bindingResult = mock(BindingResult.class);
		MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);

		// Mock the field errors
		List<FieldError> fieldErrors = List.of(
			new FieldError("productCreateRequestDto", "name", "상품명은 필수 항목입니다."),
			new FieldError("productCreateRequestDto", "description", "상품 설명은 필수 항목입니다."),
			new FieldError("productCreateRequestDto", "cityId", "도시 ID는 필수 항목입니다."),
			new FieldError("productCreateRequestDto", "id", "상품 소분류 ID는 필수 항목입니다."),
			new FieldError("productCreateRequestDto", "currencyId", "통화 ID는 필수 항목입니다.")
		);

		when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
		when(exception.getBindingResult()).thenReturn(bindingResult);

		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		WebRequest request = mock(WebRequest.class);

		// when
		ResponseEntity<Object> response = globalExceptionHandler.handleMethodArgumentNotValid(
			exception, headers, status, request);

		// then
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertInstanceOf(GlobalResponse.class, response.getBody());
		GlobalResponse globalResponse = (GlobalResponse)(response.getBody());
		assertInstanceOf(ErrorResponse.class, globalResponse.data());
		ErrorResponse errorResponse = (ErrorResponse)(globalResponse.data());
		assertEquals(ErrorCode.METHOD_ARGUMENT_INVALID.getCode(), errorResponse.code());
		assertTrue(errorResponse.message().contains(ErrorCode.METHOD_ARGUMENT_INVALID.getMessage()));
	}

	@Test
	@DisplayName("HttpRequestMethodNotSupported가 발생한 경우")
	void httpRequestMethodNotSupportedOccurred() {
		// given
		HttpRequestMethodNotSupportedException exception = mock(HttpRequestMethodNotSupportedException.class);
		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
		WebRequest request = mock(WebRequest.class);

		// when
		ResponseEntity<Object> response = globalExceptionHandler.handleHttpRequestMethodNotSupported(
			exception, headers, status, request);

		// then
		assertNotNull(response);
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
		assertInstanceOf(GlobalResponse.class, response.getBody());
		GlobalResponse globalResponse = (GlobalResponse)(response.getBody());
		assertInstanceOf(ErrorResponse.class, globalResponse.data());
		ErrorResponse errorResponse = (ErrorResponse)(globalResponse.data());
		assertEquals(ErrorCode.METHOD_NOT_SUPPORTED.getCode(), errorResponse.code());
		assertEquals(ErrorCode.METHOD_NOT_SUPPORTED.getMessage(), errorResponse.message());
	}

	@Test
	@DisplayName("ExceptionInternal이 발생한 경우")
	void exceptionInternalOccurred() {
		// given
		Exception exception = new RuntimeException("Test exception");
		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		WebRequest request = mock(WebRequest.class);

		// when
		ResponseEntity<Object> response = globalExceptionHandler.handleExceptionInternal(
			exception, null, headers, status, request);

		// then
		assertNotNull(response);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertInstanceOf(GlobalResponse.class, response.getBody());
		GlobalResponse globalResponse = (GlobalResponse)(response.getBody());
		assertInstanceOf(ErrorResponse.class, globalResponse.data());
		ErrorResponse errorResponse = (ErrorResponse)(globalResponse.data());
		assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), errorResponse.code());
		assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), errorResponse.message());
	}

	@Test
	@DisplayName("HttpMessageNotReadableException이 발생한 경우")
	void httpMessageNotReadableOccurred() {
		// given
		HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
		when(exception.getMessage()).thenReturn("Error reading HTTP message");

		HttpHeaders headers = new HttpHeaders();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		WebRequest request = mock(WebRequest.class);

		// when
		ResponseEntity<Object> response = globalExceptionHandler.handleHttpMessageNotReadable(
			exception, headers, status, request);

		// then
		assertNotNull(response);
		assertEquals(ErrorCode.HTTP_MESSAGE_NOT_READABLE.getStatus(), response.getStatusCode());
		assertInstanceOf(GlobalResponse.class, response.getBody());
		GlobalResponse globalResponse = (GlobalResponse)(response.getBody());
		assertInstanceOf(ErrorResponse.class, globalResponse.data());
		ErrorResponse errorResponse = (ErrorResponse)(globalResponse.data());
		assertEquals(ErrorCode.HTTP_MESSAGE_NOT_READABLE.getCode(), errorResponse.code());
		assertEquals(ErrorCode.HTTP_MESSAGE_NOT_READABLE.getMessage(), errorResponse.message());
	}

	@Test
	@DisplayName("CustomException이 발생한 경우")
	void customExceptionOccurred() {
		// given
		CustomException exception = new CustomException(ErrorCode.SAMPLE_ERROR);

		// when
		ResponseEntity<Object> response = globalExceptionHandler.handleCustomException(exception);

		// then
		assertNotNull(response);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertInstanceOf(GlobalResponse.class, response.getBody());
		GlobalResponse globalResponse = (GlobalResponse)(response.getBody());
		assertInstanceOf(ErrorResponse.class, globalResponse.data());
		ErrorResponse errorResponse = (ErrorResponse)(globalResponse.data());
		assertEquals(ErrorCode.SAMPLE_ERROR.getCode(), errorResponse.code());
		assertEquals(ErrorCode.SAMPLE_ERROR.getMessage(), errorResponse.message());
	}

	@Test
	@DisplayName("Exception이 발생한 경우")
	void handleException() {
		// given
		Exception exception = new RuntimeException("Unexpected exception");

		// when
		ResponseEntity<Object> response = globalExceptionHandler.handleException(exception);

		// then
		assertNotNull(response);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertNotNull(response.getBody());
		GlobalResponse globalResponse = (GlobalResponse)(response.getBody());
		ErrorResponse errorResponse = (ErrorResponse)(globalResponse.data());
		assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), errorResponse.code());
		assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), errorResponse.message());
	}
}
