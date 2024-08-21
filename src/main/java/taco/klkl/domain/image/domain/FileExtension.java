package taco.klkl.domain.image.domain;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.image.exception.FileExtensionInvalidException;

@Getter
@RequiredArgsConstructor
public enum FileExtension {
	JPEG("jpeg"),
	JPG("jpg"),
	PNG("png"),
	;

	private final String value;

	public static FileExtension from(final String value) throws FileExtensionInvalidException {
		return Arrays.stream(FileExtension.values())
			.filter(extension -> extension.getValue().equals(value))
			.findFirst()
			.orElseThrow(FileExtensionInvalidException::new);
	}
}
