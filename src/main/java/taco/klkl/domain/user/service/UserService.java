package taco.klkl.domain.user.service;

import org.springframework.stereotype.Service;

import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequest;
import taco.klkl.domain.user.dto.request.UserUpdateRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;

@Service
public interface UserService {

	UserDetailResponse getCurrentUser();

	UserDetailResponse updateUser(final UserUpdateRequest updateRequest);

	User createUser(final UserCreateRequest createRequest);

}
