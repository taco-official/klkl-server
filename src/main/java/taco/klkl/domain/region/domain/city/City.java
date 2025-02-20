package taco.klkl.domain.region.domain.city;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.region.domain.country.Country;

@Getter
@Entity(name = "city")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class City {

	@Transient
	private CityType cityType;

	@Id
	@Column(name = "city_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(
		fetch = FetchType.LAZY,
		optional = false
	)
	@JoinColumn(
		name = "country_id",
		nullable = false
	)
	private Country country;

	@Column(
		name = "name",
		length = 50,
		nullable = false
	)
	private String name;

	private City(
		final CityType cityType,
		final Country country
	) {
		this.cityType = cityType;
		this.country = country;
		this.name = cityType.getName();
	}

	public static City of(
		final CityType cityType,
		final Country country
	) {
		return new City(cityType, country);
	}
}
