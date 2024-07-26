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
public class Region {

	@Id
	@Column(name = "region_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long regionId;

	@Column(name = "name", length = 20, nullable = false)
	String name;

	private Region(String name) {
		this.name = name;
	}

	public static Region of(String name) {
		return new Region(name);
	}
}
