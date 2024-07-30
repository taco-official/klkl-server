package taco.klkl.domain.subcategory.domain;

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
import taco.klkl.domain.category.domain.Category;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subcategory {
	@Id
	@Column(name = "subcategory_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

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
