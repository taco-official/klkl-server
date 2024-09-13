package taco.klkl.domain.member.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.dto.request.MemberCreateRequest;
import taco.klkl.domain.member.dto.request.MemberUpdateRequest;
import taco.klkl.domain.member.dto.response.FollowResponse;
import taco.klkl.domain.member.dto.response.MemberDetailResponse;
import taco.klkl.domain.member.dto.response.MemberSimpleResponse;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.global.common.response.PagedResponse;

@Service
public interface MemberService {

	MemberDetailResponse getMemberById(final Long id);

	PagedResponse<ProductSimpleResponse> getMemberProductsById(final Long id, final Pageable pageable);

	PagedResponse<ProductSimpleResponse> getMemberLikesById(final Long id, final Pageable pageable);

	List<MemberSimpleResponse> getFollowings();

	FollowResponse getFollowingStatus(final Long followingId);

	Member createMember(final MemberCreateRequest createRequest);

	FollowResponse createFollow(final Long followingId);

	FollowResponse removeFollow(final Long followingId);

	MemberDetailResponse updateMember(final MemberUpdateRequest updateRequest);

}
