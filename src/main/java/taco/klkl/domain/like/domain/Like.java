package taco.klkl.domain.like.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.product.domain.Product;

@Getter
@Entity(name = "member_product_like")
@Table(
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"product_id", "member_id"})
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
		name = "member_id",
		nullable = false
	)
	private Member member;

	@Column(
		name = "created_at",
		nullable = false
	)
	private LocalDateTime createdAt;

	private Like(final Product product, final Member member) {
		this.product = product;
		this.member = member;
		this.createdAt = LocalDateTime.now();
	}

	public static Like of(final Product product, final Member member) {
		return new Like(product, member);
	}
}
