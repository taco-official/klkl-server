package taco.klkl.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.user.dto.response.UserDetailResponseDto;
import taco.klkl.domain.user.service.UserService;

@Slf4j
@RestController
@Tag(name = "1. 유저", description = "유저 관련 API")
@RequestMapping("/v1/users")
public class UserController {
	final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다. (테스트용)")
	@GetMapping("/me")
	public ResponseEntity<UserDetailResponseDto> getMe() {
		UserDetailResponseDto userDto = userService.getMyInfo();
		return ResponseEntity.ok().body(userDto);
	}
}
