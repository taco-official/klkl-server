package taco.klkl.global.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.image.dao.ImageRepository;
import taco.klkl.domain.image.domain.FileExtension;
import taco.klkl.domain.image.domain.Image;
import taco.klkl.domain.image.domain.ImageType;
import taco.klkl.domain.image.exception.ImageNotFoundException;
import taco.klkl.domain.image.exception.ImageUrlInvalidException;

@Component
@RequiredArgsConstructor
public class ImageUtil {

	private static final Pattern URL_PATTERN = Pattern.compile("https://([^/]+)/([^/]+)/(\\d+)/([^/]+)\\.(\\w+)");

	private final ImageRepository imageRepository;

	@Value("${cloud.aws.cloudfront.domain}")
	private String cloudFrontDomain;

	public Image findImageByImageUrl(final ImageType imageType, final String imageUrl) {
		final Matcher matcher = parseUrl(imageUrl);
		validateUrlFormat(matcher);
		validateUrlComponents(matcher, imageType);

		final String userId = matcher.group(3);
		final String imageKey = matcher.group(4);
		final String fileExtension = matcher.group(5);

		final Long parsedUserId = parseUserId(userId);
		final Image image = findImageByUrlComponents(imageType, parsedUserId, imageKey);
		validateFileExtension(image.getFileExtension(), fileExtension);

		return image;
	}

	private Image findImageByUrlComponents(final ImageType imageType, final Long userId, final String imageKey) {
		return imageRepository.findByImageTypeAndTargetIdAndImageKey(imageType, userId, imageKey)
			.orElseThrow(ImageNotFoundException::new);
	}

	private Matcher parseUrl(final String imageUrl) {
		return URL_PATTERN.matcher(imageUrl);
	}

	private Long parseUserId(final String userId) {
		try {
			return Long.parseLong(userId);
		} catch (NumberFormatException e) {
			throw new ImageUrlInvalidException();
		}
	}

	private void validateUrlFormat(final Matcher matcher) {
		if (!matcher.matches()) {
			throw new ImageUrlInvalidException();
		}
	}

	private void validateUrlComponents(final Matcher matcher, final ImageType expectedImageType) {
		final String domain = matcher.group(1);
		final String imageType = matcher.group(2);

		if (!domain.equals(cloudFrontDomain)) {
			throw new ImageUrlInvalidException();
		}
		if (!expectedImageType.getValue().equals(imageType)) {
			throw new ImageUrlInvalidException();
		}
	}

	private void validateFileExtension(final FileExtension imageFileExtension, final String urlFileExtension) {
		if (!imageFileExtension.getValue().equals(urlFileExtension)) {
			throw new ImageUrlInvalidException();
		}
	}
}