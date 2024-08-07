package taco.klkl.domain.category.domain;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubcategoryName {
	//식품
	INSTANT_FOOD("라면 및 즉석식품"),
	SNACK("스낵 및 과자"),
	SAUCE("조미료 및 소스"),
	HEALTH_FOOD("보충제 및 건강식품"),
	BEVERAGE("음료 및 차"),
	DRINKS("주류"),
	//의류
	TOP("상의"),
	BOTTOM("하의"),
	OUTER("아우터"),
	DRESS("원피스"),
	SHOES("신발"),
	ACCESSORY("액세사리"),
	JEWELRY("쥬얼리"),
	//잡화
	DRUG("일반의약품"),
	KITCHEN_SUPPLIES("주방잡화"),
	BATHROOM_SUPPLIES("욕실잡화"),
	STATIONERY("문구 및 완구"),
	//화장품
	SKIN_CARE("스킨케어"),
	MAKEUP("메이크업"),
	HAIR_CARE("헤어케어"),
	BODY_CARE("바디케어"),
	HYGIENE_PRODUCT("위생용품"),
	//None
	NONE(""),
	;

	private final String koreanName;

	public static SubcategoryName getByName(String name) {
		return Arrays.stream(values())
			.filter(subcategoryName -> subcategoryName.koreanName.equals(name))
			.findFirst()
			.orElse(NONE);
	}

	public static List<SubcategoryName> getSubcategoryNameByPartialString(String partial) {

		if (partial == null || partial.isEmpty()) {
			return List.of();
		}

		String regex = ".*" + Pattern.quote(partial) + ".*";
		Pattern pattern = Pattern.compile(regex);

		return Arrays.stream(SubcategoryName.values())
			.filter(c -> pattern.matcher(c.getKoreanName()).matches())
			.toList();
	}
}
