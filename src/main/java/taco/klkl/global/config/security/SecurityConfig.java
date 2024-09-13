package taco.klkl.global.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import taco.klkl.domain.member.domain.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			// disable csrf
			.csrf(AbstractHttpConfigurer::disable)

			// enable h2-console
			.headers((headers) ->
				headers.contentTypeOptions(contentTypeOptionsConfig ->
					headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)))

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
			);

		return httpSecurity.build();
	}
}
