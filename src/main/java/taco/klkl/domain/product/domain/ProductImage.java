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
import taco.klkl.domain.image.domain.Image;

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

	@ManyToOne(
		optional = false
	)
	@JoinColumn(
		name = "image_id",
		nullable = false
	)
	private Image image;

	@Column(
		name = "image_order",
		nullable = false
	)
	private Integer order;

	public static ProductImage of(
		final Product product,
		final Image image,
		final Integer order
	) {
		return new ProductImage(product, image, order);
	}

	private ProductImage(
		final Product product,
		final Image image,
		final Integer order
	) {
		this.product = product;
		this.image = image;
		this.order = order;
	}
}
