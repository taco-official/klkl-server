package taco.klkl.domain.image.domain;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.image.exception.FileExtensionNotFoundException;

@Getter
@RequiredArgsConstructor
public enum FileExtension {
	JPG("jpeg"),
	JPEG("jpeg"),
	PNG("png"),
	WEBP("webp"),
	;

	private final String value;

	public static FileExtension from(final String fileExtension) throws FileExtensionNotFoundException {
		return Arrays.stream(FileExtension.values())
			.filter(extension -> extension.getValue().equals(fileExtension))
			.findFirst()
			.orElseThrow(FileExtensionNotFoundException::new);
	}
}
