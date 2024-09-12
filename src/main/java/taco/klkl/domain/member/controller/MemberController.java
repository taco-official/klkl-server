package taco.klkl.domain.member.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.dto.request.MemberUpdateRequest;
import taco.klkl.domain.member.dto.response.FollowResponse;
import taco.klkl.domain.member.dto.response.MemberDetailResponse;
import taco.klkl.domain.member.dto.response.MemberSimpleResponse;
import taco.klkl.domain.member.service.MemberService;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.global.common.constants.ProductConstants;
import taco.klkl.global.common.response.PagedResponse;
import taco.klkl.global.util.MemberUtil;

@Slf4j
@RestController
@Tag(name = "1. 회원", description = "회원 관련 API")
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final MemberUtil memberUtil;

	@GetMapping("/me")
	@Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.")
	public MemberDetailResponse getMe() {
		final Member me = memberUtil.getCurrentMember();
		return memberService.getMemberById(me.getId());
	}

	@GetMapping("/{memberId}")
	@Operation(summary = "유저 정보 조회", description = "유저 정보를 조회합니다.")
	public MemberDetailResponse getMember(@PathVariable final Long memberId) {
		return memberService.getMemberById(memberId);
	}

	@GetMapping("/me/products")
	@Operation(summary = "내 상품 목록 조회", description = "내 상품 목록을 조회합니다.")
	public PagedResponse<ProductSimpleResponse> getMyProducts(
		@PageableDefault(size = ProductConstants.DEFAULT_PAGE_SIZE) Pageable pageable
	) {
		final Member me = memberUtil.getCurrentMember();
		return memberService.getMemberProductsById(me.getId(), pageable);
	}

	@GetMapping("/{memberId}/products")
	@Operation(summary = "회원의 상품 목록 조회", description = "회원의 상품 목록을 조회합니다.")
	public PagedResponse<ProductSimpleResponse> getMemberProducts(
		@PathVariable final Long memberId,
		@PageableDefault(size = ProductConstants.DEFAULT_PAGE_SIZE) Pageable pageable
	) {
		return memberService.getMemberProductsById(memberId, pageable);
	}

	@GetMapping("/me/likes")
	@Operation(summary = "내 종아요 목록 조회", description = "내 좋아요 목록을 조회합니다.")
	public PagedResponse<ProductSimpleResponse> getMyLikes(
		@PageableDefault(size = ProductConstants.DEFAULT_PAGE_SIZE) Pageable pageable
	) {
		final Member me = memberUtil.getCurrentMember();
		return memberService.getMemberLikesById(me.getId(), pageable);
	}

	@GetMapping("/me/following")
	@Operation(summary = "내 팔로잉 목록 조회", description = "내 팔로잉 목록을 조회합니다.")
	public List<MemberSimpleResponse> getMyFollowings() {
		return memberService.getFollowings();
	}

	@GetMapping("/me/following/{memberId}")
	@Operation(summary = "특정 회원의 팔로우 여부 조회", description = "특정 회원을 팔로우했는지 여부를 조회합니다.")
	public FollowResponse getFollowingStatus(@PathVariable final Long memberId) {
		return memberService.getFollowingStatus(memberId);
	}

	@PostMapping("/me/following/{memberId}")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "회원 팔로우", description = "회원을 팔로우합니다.")
	public FollowResponse followMember(@PathVariable final Long memberId) {
		return memberService.createFollow(memberId);
	}

	@DeleteMapping("/me/following/{memberId}")
	@Operation(summary = "회원 언팔로우", description = "회원을 언팔로우합니다")
	public FollowResponse unfollowMember(@PathVariable final Long memberId) {
		return memberService.removeFollow(memberId);
	}

	@PutMapping("/me")
	@Operation(summary = "내 정보 수정", description = "내 정보를 수정합니다.")
	public MemberDetailResponse updateMe(@Valid @RequestBody final MemberUpdateRequest request) {
		return memberService.updateMember(request);
	}
}
