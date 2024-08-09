package taco.klkl.global.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.global.common.validation.constraints.ValidUserId;

@RequiredArgsConstructor
public class UserIdValidator implements ConstraintValidator<ValidUserId, Long> {
	private final UserRepository userRepository;

	@Override
	public boolean isValid(
		final Long userId,
		final ConstraintValidatorContext context
	) {
		if (userId == null) {
			return true;
		}
		return userRepository.existsById(userId);
	}
}
