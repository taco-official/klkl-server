package taco.klkl.domain.member.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.dto.request.MemberUpdateRequest;
import taco.klkl.domain.member.dto.response.FollowResponse;
import taco.klkl.domain.member.dto.response.MemberDetailResponse;
import taco.klkl.domain.member.dto.response.MemberSimpleResponse;
import taco.klkl.domain.oauth.dto.response.OAuth2UserInfo;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.global.common.response.PagedResponse;

@Service
public interface MemberService {

	MemberDetailResponse getMemberById(final Long id);

	PagedResponse<ProductSimpleResponse> getMemberProductsById(final Long id, final Pageable pageable);

	List<MemberSimpleResponse> getFollowings();

	FollowResponse getFollowingStatus(final Long followingId);

	PagedResponse<ProductSimpleResponse> getFollowingProducts(final Pageable pageable, final Set<Long> memberIds);

	FollowResponse createFollow(final Long followingId);

	FollowResponse removeFollow(final Long followingId);

	MemberDetailResponse updateMember(final MemberUpdateRequest updateRequest);

	Member createOrGetMemberByOAuth2(final OAuth2UserInfo userInfo);
}
