package taco.klkl.domain.member.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.auth.dto.response.OAuth2UserInfo;
import taco.klkl.domain.like.domain.Like;
import taco.klkl.domain.member.dao.FollowRepository;
import taco.klkl.domain.member.dao.MemberRepository;
import taco.klkl.domain.member.domain.Follow;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.dto.request.MemberUpdateRequest;
import taco.klkl.domain.member.dto.response.FollowResponse;
import taco.klkl.domain.member.dto.response.MemberDetailResponse;
import taco.klkl.domain.member.dto.response.MemberSimpleResponse;
import taco.klkl.domain.member.exception.MemberNotFoundException;
import taco.klkl.domain.member.exception.SelfFollowNotAllowedException;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.global.common.response.PagedResponse;
import taco.klkl.global.util.LikeUtil;
import taco.klkl.global.util.MemberUtil;
import taco.klkl.global.util.ProductUtil;

@Slf4j
@Service
@Primary
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final FollowRepository followRepository;

	private final ProductUtil productUtil;
	private final MemberUtil memberUtil;
	private final LikeUtil likeUtil;

	/**
	 * 임시 나의 정보 조회
	 * name 속성이 "testUser"인 유저를 반환합니다.
	 */
	@Override
	public MemberDetailResponse getMemberById(final Long id) {
		final Member member = memberRepository.findById(id)
			.orElseThrow(MemberNotFoundException::new);
		return MemberDetailResponse.from(member);
	}

	@Override
	public PagedResponse<ProductSimpleResponse> getMemberProductsById(final Long id, final Pageable pageable) {
		validateUser(id);
		final Pageable sortedPageable = createPageableSortedByCreatedAtDesc(pageable);
		final Page<Product> userProducts = productUtil.findProductsByMemberId(id, sortedPageable);
		return PagedResponse.of(userProducts, ProductSimpleResponse::from);
	}

	@Override
	public PagedResponse<ProductSimpleResponse> getMemberLikesById(final Long id, final Pageable pageable) {
		validateUser(id);
		final Pageable sortedPageable = createPageableSortedByCreatedAtDesc(pageable);
		final Page<Like> likes = likeUtil.findLikesByMemberId(id, sortedPageable);
		final Page<Product> likedProducts = likes.map(Like::getProduct);
		return PagedResponse.of(likedProducts, ProductSimpleResponse::from);
	}

	@Override
	public List<MemberSimpleResponse> getFollowings() {
		final Member follower = memberUtil.getCurrentMember();
		return followRepository.findByFollowerId(follower.getId()).stream()
			.map(Follow::getFollowing)
			.map(MemberSimpleResponse::from)
			.toList();
	}

	@Override
	public FollowResponse getFollowingStatus(final Long followingId) {
		final Member follower = memberUtil.getCurrentMember();
		final Member following = memberRepository.findById(followingId)
			.orElseThrow(MemberNotFoundException::new);
		final boolean isFollowing = followRepository.existsByFollowerAndFollowing(follower, following);
		return FollowResponse.of(isFollowing, follower, following);
	}

	@Override
	@Transactional
	public FollowResponse createFollow(final Long followingId) {
		final Member follower = memberUtil.getCurrentMember();
		final Member following = memberRepository.findById(followingId)
			.orElseThrow(MemberNotFoundException::new);
		validateNotMe(follower, following);
		if (isFollowPresent(follower, following)) {
			return FollowResponse.of(true, follower, following);
		}
		final Follow follow = Follow.of(follower, following);
		followRepository.save(follow);
		return FollowResponse.of(true, follower, following);
	}

	@Override
	@Transactional
	public FollowResponse removeFollow(final Long followingId) {
		final Member follower = memberUtil.getCurrentMember();
		final Member following = memberRepository.findById(followingId)
			.orElseThrow(MemberNotFoundException::new);
		if (isFollowPresent(follower, following)) {
			followRepository.deleteByFollowerAndFollowing(follower, following);
		}
		return FollowResponse.of(false, follower, following);
	}

	@Override
	@Transactional
	public MemberDetailResponse updateMember(final MemberUpdateRequest updateRequest) {
		Member member = memberUtil.getCurrentMember();
		updateMemberEntity(member, updateRequest);
		return MemberDetailResponse.from(member);
	}

	@Override
	@Transactional
	public Member createOrGetMemberByOAuth2(final OAuth2UserInfo userInfo) {
		final String name = userInfo.name();
		final String provider = userInfo.provider();
		final String providerId = userInfo.providerId();

		return memberRepository.findByProviderAndProviderId(provider, providerId)
			.orElseGet(() -> {
				final String tag = generateUniqueTag(name);
				final Member newMember = Member.ofUser(name, tag, provider, providerId);
				memberRepository.save(newMember);
				newMember.updateProfileImage(userInfo.imageUrl());
				return newMember;
			});
	}

	private void updateMemberEntity(final Member member, final MemberUpdateRequest updateRequest) {
		final String name = updateRequest.name();
		final String description = updateRequest.description();

		member.update(name, description);
	}

	private Pageable createPageableSortedByCreatedAtDesc(final Pageable pageable) {
		return PageRequest.of(
			pageable.getPageNumber(),
			pageable.getPageSize(),
			Sort.by(Sort.Direction.DESC, "createdAt")
		);
	}

	private String generateUniqueTag(final String name) {
		String tag;
		do {
			tag = MemberUtil.generateRandomTag();
		} while (memberRepository.existsByNameAndTag(name, tag));
		return tag;
	}

	private boolean isFollowPresent(final Member follower, final Member following) {
		return followRepository.existsByFollowerAndFollowing(follower, following);
	}

	private void validateUser(final Long id) {
		if (!memberRepository.existsById(id)) {
			throw new MemberNotFoundException();
		}
	}

	private void validateNotMe(final Member follower, final Member following) {
		if (follower.equals(following)) {
			throw new SelfFollowNotAllowedException();
		}
	}
}
