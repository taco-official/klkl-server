package taco.klkl.domain.product.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.user.domain.User;

@Getter
@NoArgsConstructor
@Entity(name = "product")
@DynamicInsert
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "product_id")
	private Long productId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "description", length = 2000, nullable = false)
	private String description;

	@Column(name = "address", length = 100)
	@ColumnDefault("'N/A'")
	private String address;

	@Column(name = "like_count", nullable = false)
	@ColumnDefault("0")
	private Integer likeCount;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "price")
	@ColumnDefault("0L")
	private Integer price;

	@Column(name = "city_id", nullable = false)
	private Long cityId;

	@Column(name = "subcategory_id", nullable = false)
	private Long subcategoryId;

	@Column(name = "currency_id", nullable = false)
	private Long currencyId;

	@PrePersist
	protected void prePersist() {
		if (this.address == null) {
			this.address = "N/A";
		}
		if (this.likeCount == null) {
			this.likeCount = 0;
		}
		if (this.price == null) {
			this.price = 0;
		}
		this.createdAt = LocalDateTime.now();
	}

	private Product(
		final User user,
		final String name,
		final String description,
		final String address,
		final Integer price,
		final Long cityId,
		final Long subcategoryId,
		final Long currencyId
	) {
		this.user = user;
		this.name = name;
		this.description = description;
		this.address = address;
		this.price = price;
		this.cityId = cityId;
		this.subcategoryId = subcategoryId;
		this.currencyId = currencyId;
	}

	public static Product of(
		final User user,
		final String name,
		final String description,
		final String address,
		final Integer price,
		final Long cityId,
		final Long subcategoryId,
		final Long currencyId
	) {
		return new Product(user, name, description, address, price, cityId, subcategoryId, currencyId);
	}
}
