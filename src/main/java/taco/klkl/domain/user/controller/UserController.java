package taco.klkl.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.service.UserService;

@Slf4j
@RestController
@RequestMapping("api/v1/users")
public class UserController {
	final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	public ResponseEntity<User> getMe() {
		User user = userService.me();
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
}
