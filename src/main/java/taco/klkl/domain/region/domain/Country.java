package taco.klkl.domain.region.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Entity(name = "country")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Country {

	@Id
	@Column(name = "country_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long countryId;

	@ManyToOne(
		fetch = FetchType.LAZY,
		optional = false
	)
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

	@ManyToOne(
		fetch = FetchType.LAZY,
		optional = false
	)
	@JoinColumn(
		name = "currency_id",
		nullable = false
	)
	private Currency currency;

	@OneToMany(
		mappedBy = "country",
		orphanRemoval = true
	)
	private List<City> cities;

	private Country(
		final CountryType countryType,
		final Region region,
		final String flag,
		final String photo,
		final Currency currencyId
	) {
		this.region = region;
		this.name = countryType;
		this.flag = flag;
		this.photo = photo;
		this.currency = currencyId;
	}

	public static Country of(
		final CountryType countryType,
		final Region region,
		final String flag,
		final String photo,
		final Currency currency
	) {
		return new Country(countryType, region, flag, photo, currency);
	}

}
