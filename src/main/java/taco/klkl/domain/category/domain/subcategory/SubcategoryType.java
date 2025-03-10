package taco.klkl.domain.category.domain.subcategory;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import taco.klkl.domain.category.exception.subcategory.SubcategoryTypeNotFoundException;

@Getter
@AllArgsConstructor
public enum SubcategoryType {

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
	;

	private final String name;

	public static SubcategoryType from(final String name) {
		return Arrays.stream(values())
			.filter(type -> type.getName().equals(name))
			.findFirst()
			.orElseThrow(SubcategoryTypeNotFoundException::new);
	}
}
