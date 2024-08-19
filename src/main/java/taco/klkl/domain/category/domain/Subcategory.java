package taco.klkl.domain.category.domain;

import java.util.ArrayList;
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
@Entity(name = "subcategory")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subcategory {
	@Id
	@Column(name = "subcategory_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "subcategory")
	List<SubcategoryFilter> subcategoryFilters = new ArrayList<>();

	@Column(name = "name")
	private SubcategoryName name;

	private Subcategory(Category category, SubcategoryName name) {
		this.category = category;
		this.name = name;
	}

	public static Subcategory of(Category category, SubcategoryName name) {
		return new Subcategory(category, name);
	}
}
