package taco.klkl.domain.region.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long countryId;

	@ManyToOne
	@JoinColumn(
		name = "region_id",
		nullable = false
	)
	private Region region;

	@Column(
		name = "name",
		length = 20,
		nullable = false
	)
	private CountryType name;

	@Column(
		name = "flag",
		length = 500,
		nullable = false
	)
	private String flag;

	@Column(
		name = "photo",
		length = 500,
		nullable = false
	)
	private String photo;

	// TODO: 통화 클래스로 변경하기
	@Column(
		name = "currency_id",
		nullable = false
	)
	private int currencyId;

	@OneToMany(
		mappedBy = "country",
		orphanRemoval = true
	)
	private List<City> cities;

	private Country(final CountryType countryType, final Region region, final String flag, final String photo,
		final int currencyId) {
		this.region = region;
		this.name = countryType;
		this.flag = flag;
		this.photo = photo;
		this.currencyId = currencyId;
	}

	public static Country of(final CountryType countryType, final Region region, final String flag, final String photo,
		final int currencyId) {
		return new Country(countryType, region, flag, photo, currencyId);
	}

}
