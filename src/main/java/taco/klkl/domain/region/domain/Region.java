package taco.klkl.domain.region.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.region.enums.RegionType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {

	@Id
	@Column(name = "region_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long regionId;

	@Column(name = "name", length = 20, nullable = false)
	private RegionType name;

	@OneToMany(mappedBy = "region", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Country> countries = new ArrayList<>();

	private Region(RegionType region) {
		this.name = region;
	}

	public static Region of(RegionType regionType) {
		return new Region(regionType);
	}
}
