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

@Getter
@Entity(name = "country")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Country {

	private CountryType countryType;

	@Id
	@Column(name = "country_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

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
	private String name;

	@Column(
		name = "country_code",
		length = 2,
		nullable = false
	)
	private String countryCode;

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
		final String photo,
		final Currency currency
	) {
		this.countryType = countryType;
		this.region = region;
		this.name = countryType.getName();
		this.countryCode = countryType.getCode();
		this.photo = photo;
		this.currency = currency;
	}

	public static Country of(
		final CountryType countryType,
		final Region region,
		final String photo,
		final Currency currency
	) {
		return new Country(countryType, region, photo, currency);
	}

}
