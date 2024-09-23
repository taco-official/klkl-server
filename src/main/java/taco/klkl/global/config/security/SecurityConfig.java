package taco.klkl.global.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.member.domain.Role;
import taco.klkl.domain.oauth.service.CustomOAuth2UserService;
import taco.klkl.domain.oauth.service.OAuth2SuccessHandler;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService oAuth2UserService;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	private final TokenAuthenticationFilter tokenAuthenticationFilter;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
				.requestMatchers("/error", "/favicon.ico");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// disable csrf
				.csrf(AbstractHttpConfigurer::disable)

				// disable cors
				.cors(AbstractHttpConfigurer::disable)

				// disable default authentication
				.httpBasic(AbstractHttpConfigurer::disable)

				// disable default login form
				.formLogin(AbstractHttpConfigurer::disable)

				// disable default logout
				.logout(AbstractHttpConfigurer::disable)

				// disable X-Frame-Options (enable h2-console)
				.headers((headers) ->
						headers.contentTypeOptions(contentTypeOptionsConfig ->
								headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)))

				// disable session
				.sessionManagement(sessionManagement ->
						sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// request authentication & authorization
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								.requestMatchers("/", "/login/**", "/swagger-ui/**").permitAll()
								.requestMatchers(PathRequest.toH2Console()).permitAll()
								.requestMatchers(HttpMethod.POST).authenticated()
								.requestMatchers(HttpMethod.PUT).authenticated()
								.requestMatchers(HttpMethod.DELETE).authenticated()
								.requestMatchers(
										"/v1/users/me/**",
										"/v1/products/following/**",
										"/v1/notifications/**"
								).hasRole(Role.USER.name())
								.requestMatchers(
										RegexRequestMatcher.regexMatcher("/v1/products/\\d+/likes(/.*)?"))
								.hasRole(Role.USER.name())
								.requestMatchers(
										"/v1/oauth/**",
										"/v1/users/**",
										"/v1/products/**",
										"/v1/regions/**",
										"/v1/countries/**",
										"/v1/cities/**",
										"/v1/currencies/**",
										"/v1/categories/**",
										"/v1/subcategories/**",
										"/v1/search/**"
								).permitAll()
								.anyRequest().authenticated()
				)

				// oauth2
				.oauth2Login(oauth2 ->
						oauth2
								.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
								.successHandler(oAuth2SuccessHandler)
				)

				// jwt
				.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new TokenExceptionFilter(), tokenAuthenticationFilter.getClass());

		return httpSecurity.build();
	}
}
