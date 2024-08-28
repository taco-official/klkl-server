package taco.klkl.domain.image.domain;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.image.exception.UploadStateNotFoundException;

@Getter
@RequiredArgsConstructor
public enum UploadState {
	PENDING("대기중"),
	COMPLETE("완료"),
	OUTDATED("폐기예정"),
	;

	private final String value;

	public static UploadState from(final String value) throws UploadStateNotFoundException {
		return Arrays.stream(UploadState.values())
			.filter(state -> state.getValue().equals(value))
			.findFirst()
			.orElseThrow(UploadStateNotFoundException::new);
	}
}
