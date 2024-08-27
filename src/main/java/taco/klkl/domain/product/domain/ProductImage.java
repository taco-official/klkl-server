package taco.klkl.domain.product.domain;

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
@Entity(name = "product_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@Column(
		name = "image_url",
		nullable = false
	)
	private String imageUrl;

	@Column(
		name = "order_index",
		nullable= false
	)
	private Integer orderIndex;

	public static ProductImage of(
		final Product product,
		final String imageUrl,
		final Integer orderIndex
	) {
		return new ProductImage(product, imageUrl, orderIndex);
	}

	private ProductImage(
		final Product product,
		final String imageUrl,
		final Integer orderIndex
	) {
		this.product = product;
		this.imageUrl = imageUrl;
		this.orderIndex = orderIndex;
	}
}