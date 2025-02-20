package taco.klkl.infra.cloudfront;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class CloudFrontUrlGenerator {
	private static String cloudFrontDomain;

	@Value("${cloud.aws.cloudfront.domain}")
	private String injectedCloudFrontDomain;

	@PostConstruct
	private void init() {
		cloudFrontDomain = injectedCloudFrontDomain;
	}

	public static String generateUrlByFileName(final String fileName) {
		return cloudFrontDomain + "/" + fileName;
	}
}
