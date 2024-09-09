package taco.klkl.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequest;
import taco.klkl.domain.user.dto.request.UserUpdateRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;

@Service
public interface UserService {

	UserDetailResponse getUserById(final Long id);

	List<ProductSimpleResponse> getUserProductsById(final Long id);

	UserDetailResponse updateUser(final UserUpdateRequest updateRequest);

	User createUser(final UserCreateRequest createRequest);

}
