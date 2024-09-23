package taco.klkl.domain.token.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.util.StringUtils;
import taco.klkl.domain.token.domain.Token;
import taco.klkl.domain.token.exception.TokenExpiredException;
import taco.klkl.domain.token.exception.TokenGenerationFailedException;
import taco.klkl.domain.token.exception.TokenInvalidException;

@Component
@RequiredArgsConstructor
public class TokenProvider {

	private static final String KEY_ROLE = "role";

	@Value("${jwt.secret}")
	private String key;

	@Value("${jwt.expiration.access}")
	private int accessTokenExpirationInMs;

	@Value("${jwt.expiration.refresh}")
	private int refreshTokenExpirationInMs;

	private SecretKey secretKey;

	private final TokenService tokenService;

	@PostConstruct
	protected void setSecretKey() {
		secretKey = Keys.hmacShaKeyFor(key.getBytes());
	}

	public String generateAccessToken(final Authentication authentication) {
		return generateToken(authentication, accessTokenExpirationInMs);
	}

	public void generateRefreshToken(final Authentication authentication, final String accessToken) {
		final String refreshToken = generateToken(authentication, refreshTokenExpirationInMs);
		tokenService.saveOrUpdate(authentication.getName(), refreshToken, accessToken);
	}

	public Authentication getAuthentication(final String token) {
		Claims claims = parseClaims(token);
		List<SimpleGrantedAuthority> authorities = getAuthorities(claims);

		User principal = new User(claims.getSubject(), "", authorities);
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public String reissueAccessToken(final String accessToken) {
		if (StringUtils.hasText(accessToken)) {
			Token token = tokenService.findByAccessTokenOrThrow(accessToken);
			String refreshToken = token.getRefreshToken();

			if (validateToken(refreshToken)) {
				String reissueAccessToken = generateAccessToken(getAuthentication(refreshToken));
				tokenService.updateToken(reissueAccessToken, token);
				return reissueAccessToken;
			}
		}
		return null;
	}

	private String generateToken(final Authentication authentication, final int expirationInMs) {
		Date now = new Date();
		Date expiredDate = new Date(now.getTime() + expirationInMs);

		final String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining());

		try {
			return Jwts.builder()
					.subject(authentication.getName())
					.claim(KEY_ROLE, authorities)
					.issuedAt(now)
					.expiration(expiredDate)
					.signWith(secretKey)
					.compact();
		} catch (JwtException e) {
			throw new TokenGenerationFailedException();
		}
	}

	private List<SimpleGrantedAuthority> getAuthorities(final Claims claims) {
		String roles = (String) claims.get(KEY_ROLE);
		return Arrays.stream(roles.split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	public boolean validateToken(final String token) {
		if (!StringUtils.hasText(token)) {
			return false;
		}
		try {
			Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			throw new TokenExpiredException();
		} catch (JwtException | IllegalArgumentException e) {
			throw new TokenInvalidException();
		}
	}

	private Claims parseClaims(final String token) {
		try {
			return Jwts.parser().verifyWith(secretKey).build()
					.parseSignedClaims(token).getPayload();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		} catch (JwtException | IllegalArgumentException e) {
			throw new TokenInvalidException();
		}
	}
}
