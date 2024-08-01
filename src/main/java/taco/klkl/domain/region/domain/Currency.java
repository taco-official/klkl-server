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
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Currency {

	@Id
	@Column(name = "currency_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long currencyId;

	@Column(name = "code", length = 3, nullable = false)
	private String code;

	@Column(name = "flag", length = 500, nullable = false)
	private String flag;

	private Currency(String code, String flag) {
		this.code = code;
		this.flag = flag;
	}

	public Currency of(String code, String flag) {
		return new Currency(code, flag);
	}
}
