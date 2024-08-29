package taco.klkl.domain.region.domain.region;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.region.domain.country.Country;

@Getter
@Entity(name = "region")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {

	private RegionType regionType;

	@Id
	@Column(name = "region_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(
		name = "name",
		length = 20,
		nullable = false
	)
	private String name;

	@OneToMany(
		mappedBy = "region",
		orphanRemoval = true
	)
	private List<Country> countries = new ArrayList<>();

	private Region(final RegionType regionType) {
		this.regionType = regionType;
		this.name = regionType.getName();
	}

	public static Region from(final RegionType region) {
		return new Region(region);
	}
}
