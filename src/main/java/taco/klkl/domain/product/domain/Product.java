package taco.klkl.domain.product.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.like.exception.LikeCountMaximumException;
import taco.klkl.domain.like.exception.LikeCountMinimumException;
import taco.klkl.domain.product.dto.request.ProductUpdateRequestDto;
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
	private Long productId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
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

	@Column(
		name = "city_id",
		nullable = false
	)
	private Long cityId;

	@Column(
		name = "subcategory_id",
		nullable = false
	)
	private Long subcategoryId;

	@Column(
		name = "currency_id",
		nullable = false
	)
	private Long currencyId;

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
		this.likeCount = DefaultConstants.DEFAULT_INT_VALUE;
		this.createdAt = LocalDateTime.now();
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

	public void update(ProductUpdateRequestDto updateDto) {
		if (updateDto.name() != null) {
			this.name = updateDto.name();
		}
		if (updateDto.description() != null) {
			this.description = updateDto.description();
		}
		if (updateDto.address() != null) {
			this.address = updateDto.address();
		}
		if (updateDto.price() != null) {
			this.price = updateDto.price();
		}
		if (updateDto.cityId() != null) {
			this.cityId = updateDto.cityId();
		}
		if (updateDto.subcategoryId() != null) {
			this.subcategoryId = updateDto.subcategoryId();
		}
		if (updateDto.currencyId() != null) {
			this.currencyId = updateDto.currencyId();
		}
	}

	public int increaseLikeCount() throws LikeCountMaximumException {
		if (this.likeCount == Integer.MAX_VALUE) {
			throw new LikeCountMaximumException();
		}
		this.likeCount += 1;

		return this.likeCount;
	}

	public int decreaseLikeCount() throws LikeCountMinimumException {
		if (this.likeCount == 0) {
			throw new LikeCountMinimumException();
		}
		this.likeCount -= 1;

		return this.likeCount;
	}
}
