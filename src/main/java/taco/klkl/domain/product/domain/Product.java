package taco.klkl.domain.product.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

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
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.tag.Tag;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.image.domain.Image;
import taco.klkl.domain.like.exception.LikeCountBelowMinimumException;
import taco.klkl.domain.like.exception.LikeCountOverMaximumException;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.product.converter.RatingConverter;
import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.domain.currency.Currency;
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

	@OneToMany(
		mappedBy = "product",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	@OrderBy("order ASC")
	private List<ProductImage> images = new ArrayList<>();

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
		name = "member_id",
		nullable = false
	)
	private Member member;

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
	private Set<ProductTag> productTags = new HashSet<>();

	@OneToMany(
		mappedBy = "product",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<Comment> comments = new ArrayList<>();

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

	public static Product of(
		final String name,
		final String description,
		final String address,
		final Integer price,
		final Rating rating,
		final Member member,
		final City city,
		final Subcategory subcategory,
		final Currency currency
	) {
		return new Product(name, description, address, price, rating, member, city, subcategory, currency);
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

	public void addTags(final Set<Tag> tags) {
		tags.forEach(this::addTag);
	}

	public void updateTags(final Set<Tag> updatedTags) {
		this.productTags.clear();
		updatedTags.forEach(this::addTag);
	}

	public void addTag(final Tag tag) {
		final ProductTag productTag = ProductTag.of(this, tag);
		this.productTags.add(productTag);
	}

	public int increaseLikeCount() throws LikeCountOverMaximumException {
		if (this.likeCount == Integer.MAX_VALUE) {
			throw new LikeCountOverMaximumException();
		}
		this.likeCount += 1;

		return this.likeCount;
	}

	public int decreaseLikeCount() throws LikeCountBelowMinimumException {
		if (this.likeCount == 0) {
			throw new LikeCountBelowMinimumException();
		}
		this.likeCount -= 1;

		return this.likeCount;
	}

	public void updateImages(final List<Image> updateImages) {
		this.images.clear();
		IntStream.range(0, updateImages.size())
			.forEach(i -> {
				final ProductImage productImage = ProductImage.of(this, updateImages.get(i), i);
				this.images.add(productImage);
			});
	}

	public Image getMainImage() {
		if (images.isEmpty()) {
			return null;
		}
		return images.get(0).getImage();
	}

	private Product(
		final String name,
		final String description,
		final String address,
		final Integer price,
		final Rating rating,
		final Member member,
		final City city,
		final Subcategory subcategory,
		final Currency currency
	) {
		this.name = name;
		this.description = description;
		this.address = address;
		this.price = price;
		this.rating = rating;
		this.member = member;
		this.city = city;
		this.subcategory = subcategory;
		this.currency = currency;
		this.likeCount = DefaultConstants.DEFAULT_INT_VALUE;
		this.createdAt = LocalDateTime.now();
	}
}
