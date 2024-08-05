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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.DefaultConstants;
import taco.klkl.global.common.constants.ProductConstants;

@Getter
@Entity(name = "product")
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "product_id")
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(
		name = "user_id",
		nullable = false
	)
	private User user;

	@Column(
		name = "name",
		length = ProductConstants.NAME_MAX_LENGTH,
		nullable = false
	)
	private String name;

	@Column(
		name = "description",
		length = ProductConstants.DESCRIPTION_MAX_LENGTH,
		nullable = false
	)
	private String description;

	@Column(
		name = "address",
		length = ProductConstants.ADDRESS_MAX_LENGTH,
		nullable = false
	)
	@ColumnDefault(DefaultConstants.DEFAULT_STRING)
	private String address;

	@Column(
		name = "like_count",
		nullable = false
	)
	@ColumnDefault(DefaultConstants.DEFAULT_INT_STRING)
	private Integer likeCount;

	@Column(
		name = "created_at",
		nullable = false,
		updatable = false
	)
	private LocalDateTime createdAt;

	@Column(
		name = "price",
		nullable = false
	)
	@ColumnDefault(DefaultConstants.DEFAULT_INT_STRING)
	private Integer price;

	@ManyToOne(optional = false)
	@JoinColumn(
		name = "city_id",
		nullable = false
	)
	private City city;

	@ManyToOne(optional = false)
	@JoinColumn(
		name = "subcategory_id",
		nullable = false
	)
	private Subcategory subcategory;

	@ManyToOne(optional = false)
	@JoinColumn(
		name = "currency_id",
		nullable = false
	)
	private Currency currency;

	@PrePersist
	protected void prePersist() {
		if (this.address == null) {
			this.address = ProductConstants.DEFAULT_ADDRESS;
		}
		if (this.price == null) {
			this.price = ProductConstants.DEFAULT_PRICE;
		}
	}

	private Product(
		final User user,
		final String name,
		final String description,
		final String address,
		final Integer price,
		final City city,
		final Subcategory subcategory,
		final Currency currency
	) {
		this.user = user;
		this.name = name;
		this.description = description;
		this.address = address;
		this.price = price;
		this.city = city;
		this.subcategory = subcategory;
		this.currency = currency;
		this.likeCount = DefaultConstants.DEFAULT_INT_VALUE;
		this.createdAt = LocalDateTime.now();
	}

	public static Product of(
		final User user,
		final String name,
		final String description,
		final String address,
		final Integer price,
		final City city,
		final Subcategory subcategory,
		final Currency currency
	) {
		return new Product(user, name, description, address, price, city, subcategory, currency);
	}

	public void update(
		final String name,
		final String description,
		final String address,
		final Integer price,
		final City city,
		final Subcategory subcategory,
		final Currency currency
	) {
		this.name = name;
		this.description = description;
		this.address = address;
		this.price = price;
		this.city = city;
		this.subcategory = subcategory;
		this.currency = currency;
	}
}
