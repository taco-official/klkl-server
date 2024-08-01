package taco.klkl.domain.region.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.region.enums.CityType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class City {

	@Id
	@Column(name = "city_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long cityId;

	@ManyToOne
	@JoinColumn(name = "country_id", nullable = false)
	private Country country;

	@Column(name = "name", length = 50, nullable = false)
	private CityType name;

	private City(Country country, CityType name) {
		this.country = country;
		this.name = name;
	}

	public static City of(Country country, CityType name) {
		return new City(country, name);
	}
}
