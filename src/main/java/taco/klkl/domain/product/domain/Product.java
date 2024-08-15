package taco.klkl.domain.product.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.category.domain.Filter;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.product.converter.RatingConverter;
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
		name = "price",
		nullable = false
	)
	@ColumnDefault(DefaultConstants.DEFAULT_INT_STRING)
	private Integer price;

	@Column(
		name = "like_count",
		nullable = false
	)
	@ColumnDefault(DefaultConstants.DEFAULT_INT_STRING)
	private Integer likeCount;

	@Convert(converter = RatingConverter.class)
	@Column(
		name = "rating",
		precision = 3,
		scale = 1,
		nullable = false
	)
	private Rating rating;

	@ManyToOne(
		fetch = FetchType.LAZY,
		optional = false
	)
	@JoinColumn(
		name = "user_id",
		nullable = false
	)
	private User user;

	@ManyToOne(
		fetch = FetchType.LAZY,
		optional = false
	)
	@JoinColumn(
		name = "city_id",
		nullable = false
	)
	private City city;

	@ManyToOne(
		fetch = FetchType.LAZY,
		optional = false
	)
	@JoinColumn(
		name = "subcategory_id",
		nullable = false
	)
	private Subcategory subcategory;

	@ManyToOne(
		fetch = FetchType.LAZY,
		optional = false
	)
	@JoinColumn(
		name = "currency_id",
		nullable = false
	)
	private Currency currency;

	@OneToMany(
		mappedBy = "product",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private Set<ProductFilter> productFilters = new HashSet<>();

	@Column(
		name = "created_at",
		nullable = false,
		updatable = false
	)
	private LocalDateTime createdAt;

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
		final String name,
		final String description,
		final String address,
		final Integer price,
		final Rating rating,
		final User user,
		final City city,
		final Subcategory subcategory,
		final Currency currency
	) {
		this.name = name;
		this.description = description;
		this.address = address;
		this.price = price;
		this.rating = rating;
		this.user = user;
		this.city = city;
		this.subcategory = subcategory;
		this.currency = currency;
		this.likeCount = DefaultConstants.DEFAULT_INT_VALUE;
		this.createdAt = LocalDateTime.now();
	}

	public static Product of(
		final String name,
		final String description,
		final String address,
		final Integer price,
		final Rating rating,
		final User user,
		final City city,
		final Subcategory subcategory,
		final Currency currency
	) {
		return new Product(name, description, address, price, rating, user, city, subcategory, currency);
	}

	public void update(
		final String name,
		final String description,
		final String address,
		final Integer price,
		final Rating rating,
		final City city,
		final Subcategory subcategory,
		final Currency currency
	) {
		this.name = name;
		this.description = description;
		this.address = address;
		this.price = price;
		this.rating = rating;
		this.city = city;
		this.subcategory = subcategory;
		this.currency = currency;
	}

	public void addFilters(final Set<Filter> filters) {
		filters.forEach(this::addFilter);
	}

	public void updateFilters(final Set<Filter> updatedFilters) {
		this.productFilters.clear();
		updatedFilters.forEach(this::addFilter);
	}

	public void addFilter(final Filter filter) {
		ProductFilter productFilter = ProductFilter.of(this, filter);
		this.productFilters.add(productFilter);
	}
}
