package taco.klkl.domain.user.service;

import org.springframework.stereotype.Service;

import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequest;
import taco.klkl.domain.user.dto.request.UserUpdateRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;

@Service
public interface UserService {
	UserDetailResponse getCurrentUser();

	User createUser(final UserCreateRequest createRequest);

	UserDetailResponse updateUser(final UserUpdateRequest updateRequest);
}
