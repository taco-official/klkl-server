package taco.klkl.domain.comment.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.comment.annotation.ExistUser;
import taco.klkl.domain.user.dao.UserRepository;

@RequiredArgsConstructor
public class ExistUserValidator implements ConstraintValidator<ExistUser, Long> {
	private final UserRepository userRepository;

	@Override
	public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
		return userRepository.existsById(id);
	}
}
