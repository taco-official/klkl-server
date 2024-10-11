package taco.klkl.global.config.security;

import static taco.klkl.domain.member.domain.Role.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final CustomLogoutHandler customLogoutHandler;
	private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
	private final TokenAuthenticationFilter tokenAuthenticationFilter;

	@Value("${spring.application.uri}")
	private String applicationUri;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			// disable csrf
			.csrf(AbstractHttpConfigurer::disable)

			// configure cors
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))

			// disable default authentication
			.httpBasic(AbstractHttpConfigurer::disable)

			// disable default login form
			.formLogin(AbstractHttpConfigurer::disable)

			// disable X-Frame-Options (enable h2-console)
			.headers((headers) ->
				headers.contentTypeOptions(contentTypeOptionsConfig ->
					headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
			)

			// disable session
			.sessionManagement(sessionManagement ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)

			// request authentication & authorization
			.authorizeHttpRequests(authorizeRequests ->
				authorizeRequests
					.requestMatchers(HttpMethod.POST).hasAnyRole(USER.name(), ADMIN.name())
					.requestMatchers(HttpMethod.PUT).hasAnyRole(USER.name(), ADMIN.name())
					.requestMatchers(HttpMethod.DELETE).hasAnyRole(USER.name(), ADMIN.name())
					.requestMatchers(HttpMethod.GET, getUserRoleEndpoints()).hasRole(USER.name())
					.requestMatchers(HttpMethod.GET, getBothEndpoints()).permitAll()
					.requestMatchers(HttpMethod.GET, getPublicEndpoints()).permitAll()
					.anyRequest().authenticated()
			)

			// oauth2
			.oauth2Login(oauth2 ->
				oauth2
					.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
					.successHandler(oAuth2SuccessHandler)
					.authorizationEndpoint(authorization -> authorization
						.baseUri("/v1/oauth2/authorization"))
					.redirectionEndpoint(redirection -> redirection
						.baseUri("/v1/login/oauth2/code/*"))
			)

			// configure logout
			.logout(logout -> logout
				.logoutUrl("/v1/logout")
				.addLogoutHandler(customLogoutHandler)
				.logoutSuccessHandler(customLogoutSuccessHandler)
				.permitAll()
			)

			// auth exception handling
			.exceptionHandling(exception ->
				exception
					.accessDeniedHandler(accessDeniedHandler)
					.authenticationEntryPoint(authenticationEntryPoint)
			)

			// jwt exception handling
			.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

	private String[] getPublicEndpoints() {
		return SecurityEndpoint.PUBLIC.getPatterns();
	}

	private String[] getUserRoleEndpoints() {
		return SecurityEndpoint.USER_ROLE.getPatterns();
	}

	private String[] getBothEndpoints() {
		return SecurityEndpoint.BOTH.getPatterns();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of(applicationUri));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(7200L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
