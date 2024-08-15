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
import taco.klkl.domain.category.domain.Filter;

@Getter
@Entity(name = "product_filter")
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductFilter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "product_filter_id")
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
		name = "filter_id",
		nullable = false
	)
	private Filter filter;

	private ProductFilter(Product product, Filter filter) {
		this.product = product;
		this.filter = filter;
	}

	public static ProductFilter of(Product product, Filter filter) {
		return new ProductFilter(product, filter);
	}
}
