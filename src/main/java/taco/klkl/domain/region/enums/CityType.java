package taco.klkl.domain.region.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CityType {
	// 일본
	OSAKA("오사카"),
	KYOTO("교토"),
	TOKYO("도쿄"),
	FUKUOKA("후쿠오카"),
	OKINAWA("오키나와"),
	SAPPORO("삿포로"),
	NAGOYA("나고야"),

	// 중국
	HONG_KONG("홍콩"),
	SHANGHAI("상하이"),
	BEIJING("베이징"),

	// 대만
	TAIPEI("타이베이"),

	// 동남아시아 - 태국
	BANGKOK("방콕"),
	CHIANG_MAI("치앙마이"),
	PHUKET("푸켓"),

	// 동남아시아 - 베트남
	DA_NANG("다낭"),
	NHA_TRANG("나트랑"),
	HO_CHI_MINH("호치민"),
	HANOI("하노이"),

	// 동남아시아 - 필리핀
	CEBU("세부"),
	BORACAY("보라카이"),

	// 동남아시아 - 싱가포르
	SINGAPORE("싱가포르"),

	// 동남아시아 - 인도네시아
	BALI("발리"),

	// 동남아시아 - 말레이시아
	KOTA_KINABALU("코타키나발루"),
	KUALA_LUMPUR("쿠알라룸푸르"),

	NONE("");

	private final String koreanName;

	public static CityType getCityTypeByKoreanName(String koreanName) {
		return Arrays.stream(CityType.values())
			.filter(c -> c.getKoreanName().equals(koreanName))
			.findFirst()
			.orElse(NONE);
	}
}
