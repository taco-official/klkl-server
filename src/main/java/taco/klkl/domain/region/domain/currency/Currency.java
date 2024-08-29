package taco.klkl.domain.region.domain.currency;

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

	private CurrencyType currencyType;

	@Id
	@Column(name = "currency_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "code", length = 3, nullable = false)
	private String code;

	@Column(name = "unit", length = 3, nullable = false)
	private String unit;

	private Currency(final CurrencyType currencyType) {
		this.currencyType = currencyType;
		this.code = currencyType.getCode();
		this.unit = currencyType.getUnit();
	}

	public static Currency of(final CurrencyType currencyType) {
		return new Currency(currencyType);
	}
}
