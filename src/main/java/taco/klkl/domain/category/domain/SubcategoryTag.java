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
@Entity(name = "subcategory_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubcategoryTag {
	@Id
	@Column(name = "subcategory_tag_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subcategory_id")
	private Subcategory subcategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id")
	private Tag tag;

	private SubcategoryTag(final Subcategory subcategory, final Tag tag) {
		this.subcategory = subcategory;
		this.tag = tag;
	}

	public static SubcategoryTag of(final Subcategory subcategory, final Tag tag) {
		return new SubcategoryTag(subcategory, tag);
	}
}
