package taco.klkl.domain.region.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "currency")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Currency {

	@Id
	@Column(name = "currency_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "code", length = 3, nullable = false)
	private CurrencyType code;

	@Column(name = "korean_unit", length = 3, nullable = false)
	private String koreanUnit;

	private Currency(final CurrencyType code) {
		this.code = code;
		this.koreanUnit = code.getKoreanUnit();
	}

	public static Currency of(final CurrencyType code) {
		return new Currency(code);
	}
}
