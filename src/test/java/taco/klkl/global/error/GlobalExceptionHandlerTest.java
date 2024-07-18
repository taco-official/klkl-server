package taco.klkl.global.error;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;
import taco.klkl.global.response.GlobalResponse;

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
		MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
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
		assertEquals("C010", errorResponse.code());
		assertEquals("유효하지 않은 method 인자 입니다.", errorResponse.message());
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
		assertEquals("C011", errorResponse.code());
		assertEquals("지원하지 않는 HTTP method 입니다.", errorResponse.message());
	}

	@Test
	@DisplayName("ExceptionInternal이 발생한 경우")
	void ExceptionInternalOccurred() {
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
		assertEquals("C012", errorResponse.code());
		assertEquals("서버에 문제가 발생했습니다. 관리자에게 문의해주세요.", errorResponse.message());
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
		assertEquals("C000", errorResponse.code());
		assertEquals("샘플 에러입니다.", errorResponse.message());
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
		assertEquals("C012", errorResponse.code());
		assertEquals("서버에 문제가 발생했습니다. 관리자에게 문의해주세요.", errorResponse.message());
	}
}