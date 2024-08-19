package taco.klkl.domain.category.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "subcategory_filter")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubcategoryFilter {
	@Id
	@Column(name = "subcategory_filter_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subcategory_id")
	private Subcategory subcategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "filter_id")
	private Filter filter;

	private SubcategoryFilter(final Subcategory subcategory, final Filter filter) {
		this.subcategory = subcategory;
		this.filter = filter;
	}

	public static SubcategoryFilter of(final Subcategory subcategory, final Filter filter) {
		return new SubcategoryFilter(subcategory, filter);
	}
}
