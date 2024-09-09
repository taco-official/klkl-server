package taco.klkl.domain.user.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequest;
import taco.klkl.domain.user.dto.request.UserFollowRequest;
import taco.klkl.domain.user.dto.request.UserUpdateRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.domain.user.dto.response.UserFollowResponse;
import taco.klkl.domain.user.dto.response.UserSimpleResponse;
import taco.klkl.global.common.response.PagedResponse;

@Service
public interface UserService {

	UserDetailResponse getUserById(final Long id);

	PagedResponse<ProductSimpleResponse> getUserProductsById(final Long id, final Pageable pageable);

	PagedResponse<ProductSimpleResponse> getUserLikesById(final Long id, final Pageable pageable);

	List<UserSimpleResponse> getUserFollowingById(final Long id);

	User createUser(final UserCreateRequest createRequest);

	UserFollowResponse createUserFollow(final UserFollowRequest followRequest);

	UserFollowResponse removeUserFollow(final Long followerId);

	UserDetailResponse updateUser(final UserUpdateRequest updateRequest);

}
