package taco.klkl.domain.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	USER,
	ADMIN,
	;

	private static final String AUTHORITY_PREFIX = "ROLE_";

	public String getAuthority() {
		return AUTHORITY_PREFIX + name();
	}
}
