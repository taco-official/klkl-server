package taco.klkl.domain.category.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.product.domain.ProductTag;

@Getter
@Entity(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

	private TagType tagType;

	@Id
	@Column(name = "tag_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "tag")
	List<SubcategoryTag> subcategoryTags = new ArrayList<>();

	@OneToMany(mappedBy = "tag")
	Set<ProductTag> productTags = new HashSet<>();

	private Tag(final TagType tagType) {
		this.tagType = tagType;
		this.name = tagType.getName();
	}

	public static Tag of(final TagType tagType) {
		return new Tag(tagType);
	}
}
