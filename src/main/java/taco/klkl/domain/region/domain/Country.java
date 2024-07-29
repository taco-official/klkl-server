package taco.klkl.domain.region.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.region.enums.CountryType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Country {

	@Id
	@Column(name = "country_id")
	private Long countryId;

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region regionId;

	@Column(name = "name", length = 20, nullable = false)
	private CountryType name;

	@Column(name = "flag", length = 500, nullable = false)
	private String flag;

	@Column(name = "photo", length = 500, nullable = false)
	private String photo;

	// TODO: 통화 클래스로 변경하기
	@Column(name = "currency_id", nullable = false)
	private int currencyId;

	private Country(CountryType countryType, Region region, String flag, String photo, int currencyId) {
		this.countryId = countryType.getCountryCode();
		this.regionId = region;
		this.name = countryType;
		this.flag = flag;
		this.photo = photo;
		this.currencyId = currencyId;
	}

	public Country of(CountryType countryType, Region region, String flag, String photo, int currencyId) {
		return new Country(countryType, region, flag, photo, currencyId);
	}

}
