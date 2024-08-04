package taco.klkl.domain.category.domain;

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

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Filter {

	@Id
	@Column(name = "filter_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(mappedBy = "filter")
	List<SubcategoryFilter> subcategoryFilters = new ArrayList<>();

	@Column(name = "name")
	private FilterName name;

	private Filter(FilterName filterName) {
		this.name = filterName;
	}

	public static Filter of(FilterName filterName) {
		return new Filter(filterName);
	}
}
