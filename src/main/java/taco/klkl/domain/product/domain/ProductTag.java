package taco.klkl.domain.product.domain;

import org.hibernate.annotations.DynamicInsert;

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
import taco.klkl.domain.category.domain.Tag;

@Getter
@Entity(name = "product_filter")
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductTag {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "product_tag_id")
	private Long id;

	@ManyToOne(
		fetch = FetchType.LAZY,
		optional = false
	)
	@JoinColumn(
		name = "product_id",
		nullable = false
	)
	private Product product;

	@ManyToOne(
		fetch = FetchType.LAZY,
		optional = false
	)
	@JoinColumn(
		name = "tag_id",
		nullable = false
	)
	private Tag tag;

	private ProductTag(Product product, Tag tag) {
		this.product = product;
		this.tag = tag;
	}

	public static ProductTag of(Product product, Tag tag) {
		return new ProductTag(product, tag);
	}
}
