package taco.klkl.domain.token.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private String refreshToken;

	private String accessToken;

	private Token(
		final String name,
		final String refreshToken,
		final String accessToken
	) {
		this.name = name;
		this.refreshToken = refreshToken;
		this.accessToken = accessToken;
	}

	public static Token of(
		final String name,
		final String refreshToken,
		final String accessToken
	) {
		return new Token(name, refreshToken, accessToken);
	}

	public void update(
		final String refreshToken,
		final String accessToken
	) {
		this.refreshToken = refreshToken;
		this.accessToken = accessToken;
	}
}
