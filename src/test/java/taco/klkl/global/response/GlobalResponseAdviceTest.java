package taco.klkl.global.response;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletResponse;

import taco.klkl.global.common.response.GlobalResponse;
import taco.klkl.global.common.response.GlobalResponseAdvice;

class GlobalResponseAdviceTest {

	private GlobalResponseAdvice advice;

	@Mock
	private ServerHttpRequest request;

	@Mock
	private ServletServerHttpResponse response;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		advice = new GlobalResponseAdvice();
	}

	@Test
	@DisplayName("supports 메소드가 항상 true를 반환하는지 확인")
	void testSupports() {
		assertTrue(advice.supports(null, null));
	}

	@Test
	@DisplayName("성공 상태 코드(2xx)에 대해 GlobalResponse 객체를 반환하는지 확인")
	void testBeforeBodyWrite_WithSuccessStatus() {
		// Given
		Object body = new Object();
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		mockResponse.setStatus(HttpStatus.OK.value());
		when(response.getServletResponse()).thenReturn(mockResponse);

		// When
		Object result = advice.beforeBodyWrite(body, null, MediaType.APPLICATION_JSON, null, request, response);

		// Then
		assertTrue(result instanceof GlobalResponse);
		GlobalResponse globalResponse = (GlobalResponse)result;
		assertEquals(HttpStatus.OK.value(), globalResponse.status());
		assertEquals(body, globalResponse.data());
	}

	@Test
	@DisplayName("오류 상태 코드에 대해 원본 body를 그대로 반환하는지 확인")
	void testBeforeBodyWrite_WithErrorStatus() {
		// Given
		Object body = new Object();
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		mockResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		when(response.getServletResponse()).thenReturn(mockResponse);

		// When
		Object result = advice.beforeBodyWrite(body, null, MediaType.APPLICATION_JSON, null, request, response);

		// Then
		assertEquals(body, result);
	}

	@Test
	@DisplayName("body가 String 타입일 때 원본 body를 그대로 반환하는지 확인")
	void testBeforeBodyWrite_WithStringBody() {
		// Given
		String body = "Test String";
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		mockResponse.setStatus(HttpStatus.OK.value());
		when(response.getServletResponse()).thenReturn(mockResponse);

		// When
		Object result = advice.beforeBodyWrite(body, null, MediaType.APPLICATION_JSON, null, request, response);

		// Then
		assertEquals(body, result);
	}

	@Test
	@DisplayName("HttpStatus가 null일 때 (즉, 유효하지 않은 상태 코드) 원본 body를 그대로 반환하는지 확인")
	void testBeforeBodyWrite_WithNullHttpStatus() {
		// Given
		Object body = new Object();
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		mockResponse.setStatus(999); // Invalid status code
		when(response.getServletResponse()).thenReturn(mockResponse);

		// When
		Object result = advice.beforeBodyWrite(body, null, MediaType.APPLICATION_JSON, null, request, response);

		// Then
		assertEquals(body, result);
	}
}
