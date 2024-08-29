package taco.klkl.domain.category.domain.subcategory;

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
import taco.klkl.domain.category.domain.SubcategoryTag;
import taco.klkl.domain.category.domain.category.Category;

@Getter
@Entity(name = "subcategory")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subcategory {

	private SubcategoryType subcategoryType;

	@Id
	@Column(name = "subcategory_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "subcategory")
	List<SubcategoryTag> subcategoryTags = new ArrayList<>();

	private Subcategory(final Category category, final SubcategoryType subcategoryType) {
		this.category = category;
		this.subcategoryType = subcategoryType;
		this.name = subcategoryType.getName();
	}

	public static Subcategory of(final Category category, final SubcategoryType subcategoryType) {
		return new Subcategory(category, subcategoryType);
	}
}
