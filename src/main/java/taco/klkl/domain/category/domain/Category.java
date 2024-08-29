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
@Entity(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

	private CategoryType categoryType;

	@Id
	@Column(name = "category_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "category")
	private List<Subcategory> subcategories = new ArrayList<>();

	private Category(final CategoryType categoryType) {
		this.categoryType = categoryType;
		this.name = categoryType.getName();
	}

	public static Category of(final CategoryType categoryType) {
		return new Category(categoryType);
	}
}
